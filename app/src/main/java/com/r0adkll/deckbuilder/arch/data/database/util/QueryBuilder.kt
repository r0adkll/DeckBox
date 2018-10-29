package com.r0adkll.deckbuilder.arch.data.database.util

enum class Operator(val text: String) {
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    EQUAL("="),
    NOT_EQUAL("!="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    IN("IN"),
    NOT_IN("NOT IN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    BETWEEN("BETWEEN"),
    IS_NULL("IS NULL"),
    NOT_NULL("IS NOT NULL")
}

interface Condition<L, R> {

    val leftOperand: L
    val operator: Operator
    val rightOperand: R

    fun R.sql(): String {
        return when(this) {
            is String -> "\"$this\""
            is List<*> -> "(${this.joinToString { if (it is String) "\"$it\"" else it.toString()}})"
            else -> this.toString()
        }
    }
}

interface AndOr<out Q> {
    infix fun <V> and(condition: Condition<V, *>): Q
    infix fun <V> or(condition: Condition<V, *>): Q
}

interface WhereAndOr<E> : AndOr<WhereAndOr<E>>, Return<E>

interface Logical<L, R> : Condition<L, R>, AndOr<Logical<*, *>>

interface JoinAndOr<E> : AndOr<JoinAndOr<E>>, Where<E>, Return<E>

interface JoinOn<E> {
    infix fun <V> on(field: Condition<V, *>): JoinAndOr<E>
}

interface Where<E> : Return<E> {
    infix fun <V> where(condition: Condition<V, *>): WhereAndOr<E>
}

interface Join<E> {
    infix fun join(table: String): JoinOn<E>
}

interface Return<R> {
    fun get(): R
}

class FieldCondition(
        override val leftOperand: String,
        override val operator: Operator,
        override val rightOperand: Any?
): Condition<String, Any?> {

    override fun toString(): String {
        return "$leftOperand ${operator.text}${rightOperand?.let { " ${it.sql()}" } ?: ""}"
    }
}

class AndOrCondition(
        val andOrOperator: Operator,
        condition: Condition<String, Any?>
) : Condition<String, Any?> {
    override val leftOperand: String = condition.leftOperand
    override val operator: Operator = condition.operator
    override val rightOperand: Any? = condition.rightOperand

    override fun toString() = "${andOrOperator.text} $leftOperand ${operator.text}${rightOperand?.let { " ${it.sql()}" } ?: ""}"
}

@Suppress("UNCHECKED_CAST")
class LogicalCondition(
        condition: Condition<String, Any?>,
        val conditions: List<Condition<String, Any?>> = listOf(condition)
) : Logical<String, Any?> {

    override val leftOperand: String = condition.leftOperand
    override val operator: Operator = condition.operator
    override val rightOperand: Any? = condition.rightOperand

    override fun <V> and(condition: Condition<V, *>): Logical<String, Any?> {
        val andOrCondition = AndOrCondition(Operator.AND, condition as Condition<String, Any?>)
        return LogicalCondition(andOrCondition, conditions.plus(andOrCondition))
    }

    override fun <V> or(condition: Condition<V, *>): Logical<String, Any?> {
        val andOrCondition = AndOrCondition(Operator.OR, condition as Condition<String, Any?>)
        return LogicalCondition(andOrCondition, conditions.plus(andOrCondition))
    }

    override fun toString(): String {
        return if (conditions.size > 1) {
            val builder = StringBuilder("(")
            conditions.forEachIndexed { index, c->
                if (index > 0) builder.append(" ")
                builder.append(c.toString())
            }
            builder.append(")")
            builder.toString()
        } else {
            "$leftOperand ${operator.text}${rightOperand?.let { " ${it.sql()}" } ?: ""}"
        }
    }
}



abstract class Expression(protected val statement: Statement): Return<String> {

    abstract fun build(): String

    override fun get(): String {
        return statement.get()
    }
}

class JoinExpression(
        statement: Statement,
        private val table: String
): Expression(statement), JoinOn<String> {

    override fun <V> on(field: Condition<V, *>): JoinAndOr<String> {
        val expression = JoinOnExpression(statement, field)
        statement.expressions += expression
        return expression
    }

    override fun build(): String {
        return "INNER JOIN $table"
    }
}

class JoinOnExpression<V>(
        statement: Statement,
        private val field: Condition<V, *>,
        private val operator: Operator? = null
) : Expression(statement), JoinAndOr<String> {

    override fun <V> and(condition: Condition<V, *>): JoinAndOr<String> {
        val expression = JoinOnExpression(statement, condition, Operator.AND)
        statement.expressions += expression
        return expression
    }

    override fun <V> or(condition: Condition<V, *>): JoinAndOr<String> {
        val expression = JoinOnExpression(statement, condition, Operator.AND)
        statement.expressions += expression
        return expression
    }

    override fun <V> where(condition: Condition<V, *>): WhereAndOr<String> {
        val expression = WhereExpression(statement, condition)
        statement.expressions += expression
        return expression
    }

    override fun build(): String {
        return "${operator?.text?.let { "$it " } ?: "ON "}$field"
    }
}

open class LogicalExpression<V>(
        statement: Statement,
        private val condition: Condition<V, *>,
        private val operator: Operator? = null
): Expression(statement), WhereAndOr<String> {

    override fun <V> and(condition: Condition<V, *>): WhereAndOr<String> {
        val expression = LogicalExpression(statement, condition, Operator.AND)
        statement.expressions += expression
        return expression
    }

    override fun <V> or(condition: Condition<V, *>): WhereAndOr<String> {
        val expression = LogicalExpression(statement, condition, Operator.OR)
        statement.expressions += expression
        return expression
    }

    override fun build(): String {
        return "${operator?.text?.let { "$it " } ?: ""}$condition"
    }
}

class WhereExpression<V>(
        statement: Statement,
        condition: Condition<V, *>
) : LogicalExpression<V>(statement, condition, null) {

    override fun build(): String {
        return "WHERE ${super.build()}"
    }
}

class Statement internal constructor(private val table: String) : Join<String>, Where<String> {

    internal val expressions = ArrayList<Expression>()


    override fun join(table: String): JoinOn<String> {
        val expression = JoinExpression(this, table)
        expressions += expression
        return expression
    }


    override fun <V> where(condition: Condition<V, *>): WhereAndOr<String> {
        val expression = WhereExpression(this, condition)
        expressions += expression
        return expression
    }


    override fun get(): String {
        val builder = StringBuilder("SELECT * FROM $table")

        expressions.forEach {
            builder.append(" ")
            builder.append(it.build())
        }

        return builder.toString()
    }
}

infix fun String.eq(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.EQUAL, other))
infix fun String.ne(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.NOT_EQUAL, other))
infix fun String.lt(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.LESS_THAN, other))
infix fun String.gt(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.GREATER_THAN, other))
infix fun String.lte(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.LESS_THAN_OR_EQUAL, other))
infix fun String.gte(other: Any): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.GREATER_THAN_OR_EQUAL, other))
infix fun String.like(other: String): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.LIKE, other))
fun String.`in`(other: List<Any>): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.IN, other))
fun String.notIn(other: List<Any>): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.NOT_IN, other))
fun String.isNull(): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.IS_NULL, null))
fun String.notNull(): Logical<String, Any?> = LogicalCondition(FieldCondition(this, Operator.NOT_NULL, null))

/**
 * Query helper for a syntatic sugary way of building query strings
 */
object Query {

    fun select(table: String): Statement {
        return Statement(table)
    }
}