package com.eduardoagabes.currencyconverter

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eduardoagabes.currencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val coins = listOf(
            DataConversor(R.drawable.ic_brazil, "BRL", 5.0),
            DataConversor(R.drawable.ic_uk, "GBP", 0.75),
            DataConversor(R.drawable.ic_usa, "USD", 1.0),
            DataConversor(R.drawable.ic_ue, "EUR", 0.85)
        )

        class CustomSpinnerAdapter(context: Context, private val items: List<DataConversor>) :
            ArrayAdapter<DataConversor>(context, 0, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createViewFromResource(position, convertView, parent)
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return createViewFromResource(position, convertView, parent)
            }

            private fun createViewFromResource(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.spinner_item, parent, false)
                val item = getItem(position)

                val imageView = view.findViewById<ImageView>(R.id.imageView)
                val textView = view.findViewById<TextView>(R.id.textView)

                imageView.setImageResource(item?.image ?: 0)
                textView.text = item?.coin

                return view
            }
        }

        val adapter = CustomSpinnerAdapter(this, coins)
        binding.spinnerAmount.adapter = adapter
        binding.spinnerConverted.adapter = adapter
        binding.spinnerConverted.setSelection(1)


        binding.tieAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val inputValue = s.toString().toDoubleOrNull() ?: 0.0

                val originCurrency = binding.spinnerAmount.selectedItem as DataConversor
                val destinationCurrency = binding.spinnerConverted.selectedItem as DataConversor

                val resultado = (inputValue * originCurrency.worth) / destinationCurrency.worth

                binding.tvResult.text = String.format(
                    "%.2f %s",
                    resultado,
                    destinationCurrency.coin
                )

            }

        })

        binding.fabReverse.setOnClickListener {
            val selectedOriginCurrency = binding.spinnerAmount.selectedItem as DataConversor
            val selectedDestinationCurrency = binding.spinnerConverted.selectedItem as DataConversor

            binding.spinnerAmount.setSelection(coins.indexOf(selectedDestinationCurrency))
            binding.spinnerConverted.setSelection(coins.indexOf(selectedOriginCurrency))

            val animator = ObjectAnimator.ofFloat(binding.fabReverse, "rotation", 0f, 90f)
            animator.duration = 300
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()

            updateConversion()
        }

        binding.btnLimpiar.setOnClickListener {
            binding.tieAmount.setText("")
            binding.tvResult.text = ""
        }
1
    }

    private fun updateConversion() {
        val inputValue = binding.tieAmount.text.toString().toDoubleOrNull() ?: 0.0
        val originCurrency = binding.spinnerAmount.selectedItem as DataConversor
        val destinationCurrency = binding.spinnerConverted.selectedItem as DataConversor

        val resultado = (inputValue * originCurrency.worth) / destinationCurrency.worth
        binding.tvResult.text = String.format(
            "%.2f %s",
            resultado,
            destinationCurrency.coin
        )
    }
}