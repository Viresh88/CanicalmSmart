// LanguageAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.assignmentshaaysoft.LanguageItem
import com.example.assignmentshaaysoft.R

class LanguageAdapter(context: Context, languageList: List<LanguageItem>) :
    ArrayAdapter<LanguageItem>(context, 0, languageList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        val languageItem = getItem(position)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)

        languageItem?.let {
            imageView.setImageResource(it.flagImage)
            textView.text = it.languageName
        }

        return view
    }
}
