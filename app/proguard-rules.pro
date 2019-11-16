# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---== App Rules ==---
# Models
-keep public class com.r0adkll.deckbuilder.arch.data.database.entities.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.data.database.relations.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.data.features.collection.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.data.features.community.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.data.features.decks.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.data.features.marketplace.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.domain.features.remote.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.domain.expansions.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.domain.features.importer.model.** { *; }
-keep public class com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.** { *; }

# pokemontcg.io kotlin sdk rules
-keep public class io.pokemontcg.internal.api.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
