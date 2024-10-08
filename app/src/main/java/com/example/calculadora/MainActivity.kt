package com.example.calculadora

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityMainBinding

    // Variáveis
    private var primeiroNumero = ""
    private var numeroAtual = ""
    private var operadorAtual = ""
    private var resultado = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Limitar tela
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Views
        binding.apply {
            // Obter todos os botões
            LayoutMain.children.filterIsInstance<Button>().forEach { botao ->
                // Clique dos botões
                botao.setOnClickListener {
                    // Obter o texto do botão clicado
                    val textoDoBotao = botao.text.toString()
                    when {
                        textoDoBotao.matches(Regex("[0-9]")) -> {
                            if (operadorAtual.isEmpty()) {
                                primeiroNumero += textoDoBotao
                                tvResult.text = primeiroNumero
                            } else {
                                numeroAtual += textoDoBotao
                                tvResult.text = numeroAtual
                            }
                        }
                        textoDoBotao.matches(Regex("[+\\-*/]")) -> {
                            // Limpa o número atual
                            numeroAtual = ""
                            // Verifica se há algum texto no TextView tvResult
                            if (tvResult.text.toString().isNotEmpty()) {
                                // Atualiza o operador atual e mostra '0' na tela
                                operadorAtual = textoDoBotao
                                tvResult.text = "0"
                            }
                        }
                        // Se o texto do botão é '=', realiza o cálculo da expressão
                        textoDoBotao == "=" -> {
                            if (numeroAtual.isNotEmpty() && operadorAtual.isNotEmpty()) {
                                // Exibe a fórmula completa no TextView tvFormula
                                tvFormula.text = "$primeiroNumero$operadorAtual$numeroAtual"
                                // Avalia a expressão e armazena o resultado
                                resultado = avaliarExpressao(primeiroNumero, numeroAtual, operadorAtual)
                                // Atualiza o primeiro número com o resultado
                                primeiroNumero = resultado
                                // Mostra o resultado na tela (TextView tvResult)
                                tvResult.text = resultado
                                // Reseta o operadorAtual
                                operadorAtual = ""
                            }
                        }
                        // Se o texto do botão é '.', adiciona ponto decimal no número
                        textoDoBotao == "." -> {
                            if (operadorAtual.isEmpty()) {
                                // Se não há operador, verifica se o primeiro número já contém ponto decimal
                                if (!primeiroNumero.contains(".")) {
                                    // Adiciona ponto decimal ao primeiro número
                                    if (primeiroNumero.isEmpty()) primeiroNumero += "0$textoDoBotao"
                                    else primeiroNumero += textoDoBotao
                                    // Atualiza o texto na tela (TextView tvResult)
                                    tvResult.text = primeiroNumero
                                }
                            } else {
                                // Se há operador, verifica se o número atual já contém ponto decimal
                                if (!numeroAtual.contains(".")) {
                                    // Adiciona ponto decimal ao número atual
                                    if (numeroAtual.isEmpty()) numeroAtual += "0$textoDoBotao"
                                    else numeroAtual += textoDoBotao
                                    // Atualiza o texto na tela (TextView tvResult)
                                    tvResult.text = numeroAtual
                                }
                            }
                        }
                        // Se o texto do botão for 'C', limpa todos os valores e reinicia
                        textoDoBotao == "C" -> {
                            numeroAtual = ""
                            primeiroNumero = ""
                            operadorAtual = ""
                            // Mostra '0' na tela (TextView tvResult)
                            tvResult.text = "0"
                            // Limpa a fórmula na tela (TextView tvFormula)
                            tvFormula.text = ""
                        }
                    }
                }
            }
        }
    }

    // Função para avaliar a expressão matemática e retornar o resultado como string
    private fun avaliarExpressao(primeiroNumero: String, segundoNumero: String, operador: String): String {
        // Converte os números de string para double
        val num1 = primeiroNumero.toDouble()
        val num2 = segundoNumero.toDouble()
        // Realiza a operação de acordo com o operador selecionado
        return when (operador) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> (num1 / num2).toString()
            else -> ""
        }
    }
}
