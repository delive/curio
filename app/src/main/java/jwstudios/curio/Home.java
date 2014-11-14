package jwstudios.curio;

import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import jwstudios.curio.data.Question;
import jwstudios.curio.data.User;
import jwstudios.curio.persistence.PersistenceManager;


public class Home
        extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        displayTestContent();
    }

    private void displayTestContent() {
        final TextView textView = (TextView) findViewById(R.id.home_content);
        final Context context = getApplicationContext();
        final PersistenceManager manager = PersistenceManager.get();

        final StringBuilder sb = new StringBuilder();

        sb.append("USERS: \n");

        for (final User user : manager.getUsers(context)) {
            sb.append(user).append("\n");
        }

        sb.append("\n QUESTIONS and ANSWERS:\n\n");

        final Collection<Question> questions = Question.getAllFullQuestions(context);

        for (final Question question : questions) {
            sb.append(question).append("\n\n");
        }

        textView.setText(sb.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
