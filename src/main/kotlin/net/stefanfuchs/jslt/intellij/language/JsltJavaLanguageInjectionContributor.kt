package net.stefanfuchs.jslt.intellij.language

import com.intellij.lang.injection.general.Injection
import com.intellij.lang.injection.general.LanguageInjectionContributor
import com.intellij.lang.injection.general.SimpleInjection
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl

class JsltJavaLanguageInjectionContributor : LanguageInjectionContributor {

    override fun getInjection(context: PsiElement): Injection? {
        // Handle Java string literals
        if (context is PsiLiteralExpressionImpl) {
            val text = context.text
            // Check if it's a multiline string (text block in Java 13+) or regular string starting with // JSLT
            if (text.startsWith("\"\"\"") && text.contains("// JSLT")) {
                return SimpleInjection(JsltLanguage, "", "", null)
            }
        }
        return null
    }
}
