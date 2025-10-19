package net.stefanfuchs.jslt.intellij.language

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.childrenOfType
import com.intellij.util.ProcessingContext
import com.schibsted.spt.data.jslt.impl.BuiltinFunctions
import net.stefanfuchs.jslt.intellij.language.psi.JsltFunctionDecl
import net.stefanfuchs.jslt.intellij.language.psi.JsltTypes

class JsltCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(JsltTypes.IDENT),
            object : CompletionProvider<CompletionParameters>() {
                public override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet,
                ) {
                    // Add language keywords
                    resultSet.addElement(LookupElementBuilder.create("false").bold())
                    resultSet.addElement(LookupElementBuilder.create("true").bold())
                    resultSet.addElement(LookupElementBuilder.create("null").bold())
                    resultSet.addElement(LookupElementBuilder.create("let").bold())
                    resultSet.addElement(LookupElementBuilder.create("if").bold())
                    resultSet.addElement(LookupElementBuilder.create("else").bold())
                    resultSet.addElement(LookupElementBuilder.create("for").bold())
                    resultSet.addElement(LookupElementBuilder.create("import").bold())
                    resultSet.addElement(LookupElementBuilder.create("def").bold())

                    // Add built-in functions with documentation
                    BuiltinFunctions.functions.keys.sorted().forEach { funcName ->
                        val doc = getBuiltinFunctionDocumentation(funcName)
                        resultSet.addElement(
                            LookupElementBuilder
                                .create(funcName)
                                .withIcon(AllIcons.Nodes.Function)
                                .withTypeText("built-in", true)
                                .appendTailText("()", true)
                                .withTailText(if (doc.isNotEmpty()) " - $doc" else "", true)
                        )
                    }

                    // Add user-defined functions
                    parameters
                        .originalFile
                        .childrenOfType<JsltFunctionDecl>()
                        .forEach {
                            val paramCount = it.functionDeclParamList?.functionDeclParamDeclList?.size ?: 0
                            val paramText = if (paramCount == 0) "()" else "($paramCount param${if (paramCount > 1) "s" else ""})"
                            resultSet.addElement(
                                LookupElementBuilder
                                    .create(it.name!!)
                                    .withIcon(AllIcons.Nodes.Method)
                                    .withTypeText(it.containingFile.name)
                                    .appendTailText(paramText, true)
                            )
                        }
                }
            }
        )
    }

    private fun getBuiltinFunctionDocumentation(funcName: String): String {
        return when (funcName) {
            "size" -> "Returns the size of an array, object, or string"
            "string" -> "Converts value to string"
            "number" -> "Converts value to number"
            "boolean" -> "Converts value to boolean"
            "array" -> "Converts value to array or creates empty array"
            "object" -> "Creates an object or converts value to object"
            "is-array" -> "Tests if value is an array"
            "is-object" -> "Tests if value is an object"
            "is-string" -> "Tests if value is a string"
            "is-number" -> "Tests if value is a number"
            "is-boolean" -> "Tests if value is a boolean"
            "is-null" -> "Tests if value is null"
            "get-key" -> "Gets value for a key from an object"
            "contains" -> "Tests if array/object contains value/key"
            "sum" -> "Sums numbers in an array"
            "min" -> "Returns minimum value from array"
            "max" -> "Returns maximum value from array"
            "round" -> "Rounds a number to nearest integer"
            "floor" -> "Rounds down to nearest integer"
            "ceiling" -> "Rounds up to nearest integer"
            "random" -> "Returns random number between 0 and 1"
            "lowercase" -> "Converts string to lowercase"
            "uppercase" -> "Converts string to uppercase"
            "capitalize" -> "Capitalizes first letter of string"
            "split" -> "Splits string by delimiter"
            "join" -> "Joins array elements into string"
            "trim" -> "Removes whitespace from string ends"
            "replace" -> "Replaces occurrences in string"
            "test" -> "Tests string against regex pattern"
            "capture" -> "Captures regex groups from string"
            "starts-with" -> "Tests if string starts with prefix"
            "ends-with" -> "Tests if string ends with suffix"
            "from-json" -> "Parses JSON string"
            "to-json" -> "Converts value to JSON string"
            "parse-time" -> "Parses time string to timestamp"
            "format-time" -> "Formats timestamp to string"
            "now" -> "Returns current timestamp"
            "zip" -> "Combines multiple arrays element-wise"
            "zip-with-index" -> "Adds index to array elements"
            "flatten" -> "Flattens nested arrays"
            "error" -> "Throws an error with message"
            "fallback" -> "Returns first non-null value"
            else -> ""
        }
    }
}
