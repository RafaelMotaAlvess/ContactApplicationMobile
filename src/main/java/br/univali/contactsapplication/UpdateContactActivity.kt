package br.univali.contactsapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.univali.contactsapplication.databinding.ActivityUpdateContactBinding

class UpdateContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateContactBinding
    private lateinit var db: ContactDatabaseHelper
    private var contactId: Int = -1
    private var phoneFieldList = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ContactDatabaseHelper(this)

        contactId = intent.getIntExtra("contact_id", -1)
        if (contactId == -1) {
            finish()
            return
        }

        val contact = db.getContactByID(contactId)
        binding.updateTitleEditText.setText(contact.title)

        contact.content.forEach { phoneNumber ->
            addPhoneField(phoneNumber)
        }

        binding.updateAddPhoneButton.setOnClickListener {
            addPhoneField("")
        }

        binding.updateSaveButton.setOnClickListener { it: View ->
            val newTitle = binding.updateTitleEditText.text.toString()

            val phoneList = phoneFieldList.map { it.text.toString() }.filter { it.isNotBlank() }

            if (phoneList.isNotEmpty()) {
                val updatedContact = Contact(contactId, newTitle, phoneList)
                db.updateContact(updatedContact)
                finish()
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please add at least one phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPhoneField(phoneNumber: String) {
        val phoneEditText = EditText(this)
        phoneEditText.hint = "Enter phone number"
        phoneEditText.textSize = 18f
        phoneEditText.setPadding(12, 12, 12, 12)
        phoneEditText.background = getDrawable(R.drawable.orange_border)
        phoneEditText.setText(phoneNumber)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 16, 0, 16)

        phoneEditText.layoutParams = params
        binding.updatePhoneContainer.addView(phoneEditText)
        phoneFieldList.add(phoneEditText)
    }

}
