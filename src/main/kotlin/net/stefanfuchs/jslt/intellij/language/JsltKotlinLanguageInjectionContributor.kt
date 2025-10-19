package net.stefanfuchs.jslt.intellij.language

import com.intellij.lang.injection.general.Injection
import com.intellij.lang.injection.general.LanguageInjectionContributor
import com.intellij.lang.injection.general.SimpleInjection
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

class JsltKotlinLanguageInjectionContributor : LanguageInjectionContributor {

    override fun getInjection(context: PsiElement): Injection? {
        // Handle Kotlin string templates (multiline strings)
        if (context is KtStringTemplateExpression) {
            val text = context.text
            // Check if it's a multiline string (triple-quoted) starting with // JSLT
            if (text.startsWith("\"\"\"") && text.contains("// JSLT")) {
                return SimpleInjection(JsltLanguage, "", "", null)
            }
        }
        return null
    }
}
