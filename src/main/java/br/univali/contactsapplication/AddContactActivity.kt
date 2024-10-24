package br.univali.contactsapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.univali.contactsapplication.databinding.ActivityAddContactBinding

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding
    private lateinit var db: ContactDatabaseHelper
    private var phoneFieldList = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ContactDatabaseHelper(context = this)

        addPhoneField()

        binding.addPhoneButton.setOnClickListener {
            addPhoneField()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()

            val phoneList = phoneFieldList.map { it.text.toString() }.filter { it.isNotBlank() }

            if (phoneList.isNotEmpty()) {
                val contact = Contact(0, title, phoneList)
                db.insertContact(contact)
                finish()
                Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please add at least one phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPhoneField() {
        val phoneEditText = EditText(this)
        phoneEditText.hint = "Enter phone number"
        phoneEditText.textSize = 18f
        phoneEditText.setPadding(12, 12, 12, 12)
        phoneEditText.background = getDrawable(R.drawable.orange_border)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 16, 0, 16)

        phoneEditText.layoutParams = params
        binding.phoneContainer.addView(phoneEditText)
        phoneFieldList.add(phoneEditText)
    }

}
