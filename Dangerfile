## METHODS DECLARATION ##
def checkForFileAndroid(file)
  ext = File.extname(file)
  case ext
  # Warn when a file .gradle is modified
  when ".gradle"
    message("`#{file}` was modified")
  end
  # Warn when a FileManifest.xml is modified
  message("`#{file}` was modified") if file =~ /AndroidManifest\.xml/
end

def exceptionMessages(file)
  if File.file?(file)
    message "Something went wrong checking `#{file}`. Check your Dangerfile"
  else
    message "One of modified files could not be read, does it really exist?"
  end
end

danger.import_dangerfile(gem: "52inc-danger")

# --------------------------------------------------------------------------------------------------------------------
# ktlint
# --------------------------------------------------------------------------------------------------------------------
checkstyle_format.base_path = Dir.pwd

ktlint_reports_dir = "app/build/reports/ktlint/*.xml"
Dir[ktlint_reports_dir].each do |file_name|
    checkstyle_format.report file_name
end

# --------------------------------------------------------------------------------------------------------------------
# Kotlin Detekt
# --------------------------------------------------------------------------------------------------------------------
detekt_reports_dir = "app/build/reports/detekt/*.xml"
Dir[detekt_reports_dir].each do |file_name|
  checkstyle_format.report file_name
end

# --------------------------------------------------------------------------------------------------------------------
# Android Lint
# --------------------------------------------------------------------------------------------------------------------
lint_reports_dir = "app/build/reports/lint-*.xml"
Dir[lint_reports_dir].each do |file_name|
    android_lint.report_file = file_name
end
android_lint.skip_gradle_task = true
android_lint.severity = "Error"
android_lint.lint(inline_mode: true)

# --------------------------------------------------------------------------------------------------------------------
# Check for CHANGELOG and whatsnew
# --------------------------------------------------------------------------------------------------------------------



# --------------------------------------------------------------------------------------------------------------------
# Check to see if we added tests for new code or code changes
# --------------------------------------------------------------------------------------------------------------------
has_app_changes = !git.modified_files.grep("/app/src/main/java/").empty?
has_test_changes = !git.modified_files.grep("/app/src/test/java/").empty?

if has_app_changes && !has_test_changes && git.lines_of_code > 20
  warn("Tests were not updated. This is fine if only refactoring, otherwise please add tests!", sticky: false)
end

#Check modified files, apply rules to them
git.modified_files.each do |file|
  begin
    checkForFileAndroid(file)
  rescue
    exceptionMessages(file)
  end
end
