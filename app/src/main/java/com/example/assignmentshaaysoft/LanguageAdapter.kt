import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.assignmentshaaysoft.LanguageItem
import com.example.assignmentshaaysoft.R

class LanguageAdapter(val context: Context, val languageList: List<LanguageItem>) : BaseAdapter(),
    Filterable {

    override fun getCount(): Int = languageList.size

    override fun getItem(position: Int): Any = languageList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.language_item, parent, false)

        val languageItem = languageList[position]

        val flagImageView: ImageView = view.findViewById(R.id.flagImageView)
        val languageTextView: TextView = view.findViewById(R.id.languageTextView)

        flagImageView.setImageResource(languageItem.flagImage)
        languageTextView.text = languageItem.languageName

        return view
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
