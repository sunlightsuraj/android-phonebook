package np.info.suraj.app.phonebook;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Person;

/**
 * Created by suraj on 1/19/16.
 * Email suraj.sht@hotmail.com
 */
public class CustomListAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<Person> persons;
    ImageView icon;
    /*private final ArrayList<String> itemname;
    private final ArrayList<Integer> imgid;*/

    public CustomListAdapter(Activity context, ArrayList<Person> persons) {
        super(context, R.layout.all_contact_list_item, persons);
        this.persons = persons;
        this.context = context;
        /*this.itemname = itemname;
        this.imgid = imgid;*/

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.all_contact_list_item, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        icon = (ImageView) rowView.findViewById(R.id.icon);

        Person person = persons.get(position);

        name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
        if(!person.getImagePath().equals("")) {
            //icon.setImageURI(person.getImage());
            //new PersonImageTask().execute(person.getImage());
            icon.setImageBitmap(ThumbnailUtils
                    .extractThumbnail(BitmapFactory.decodeFile(person.getImagePath()),
                            60, 60));
        } else {
            icon.setImageResource(R.drawable.user);
        }

        return rowView;
    }
}
