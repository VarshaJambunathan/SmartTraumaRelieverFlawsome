package com.example.jaisa.smarttraumareliever_flawsome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jaisa.smarttraumareliever_flawsome.Adapters.LawsAdapter;
import com.example.jaisa.smarttraumareliever_flawsome.Helpers.DBHelper;
import com.example.jaisa.smarttraumareliever_flawsome.Helpers.SPHelper;

import java.util.ArrayList;

public class CrimeDetailsActivity extends AppCompatActivity {
    private Button lodgeFIRButton, additionalCommentsButton, additionalCommentsDoneButton;
    private LinearLayout additionalCommentsView, remainderView;
    private EditText additionalCommentsEditText;
    private String additionalComments = "", incident="";
    private RecyclerView mLawsView;
    private RecyclerView.LayoutManager mLawsLayoutManager;
    private RecyclerView.Adapter mLawsAdapter;
    private ArrayList<String> mLawNames = new ArrayList<>();
    private ArrayList<String> mLawDescriptions = new ArrayList<>();
    private String[] lawsViolated, lawsViolationDescriptions;
    private int sizeOfLawsViolated;

    String[] crimeTitles = {"300. Murder",
            "304B. Dowry Death",
            "354A. Sexual harassment and punishment for sexual harassment",
            "326A. Voluntarily causing grievous hurt by use of acid, etc.",
            "336. Act endangering life or personal safety of others",
            "350. Criminal force",
            "354. Assault or criminal force to woman with intent to outrage her modesty",
            "354D. Stalking",
            "366. Kidnapping, abducting or inducing woman to compel her marriage, etc",
            "493. Cohabitation caused by a man deceitfully inducing a belief of lawful marriage",
            "494. Marrying again during lifetime of husband or wife",
            "498A. Husband or relative of husband of a woman subjecting her to cruelty",
            "Maternity Benefits Act 1961",
            "Equal Renumeration Act, 1976",
            "509. Word, gesture or act intended to insult the modesty of a woman",
            "The Indecent Representation of Women (Prohibition) Act, 1986"};

    String[] crimeDescriptions = {"Culpable homicide leads to murder when:\n1. If the act by which the death is caused by the intention of causing death.\n2. If it is done with intention of causing such bodily injury as the offender knows of likely to cause the death of the person to whom the harm is caused.\n3. If it is done with the intention of causing bodily injury to any person and the bodily injury intended to be inflicted is sufficient in the ordinary course of nature to cause death.\n4. If the person committing the act knows that it is so imminently dangerous that it must, in all probability, cause death or such bodily injury as is likely to cause death, and commits such act without any excuse for incurring the risk of causing death or such injury as aforesaid.\n",
            "Where the death of a woman is caused by any burns or bodily injury or occurs otherwise than under normal circumstances within seven years of her marriage and it is shown that soon before her death she was subjected to cruelty or harassment by her husband or any relative of her husband for, or in connection with, any demand for dowry, such death shall be called “dowry death” and such husband or relatives shall be deemed to have caused her death.",
            "A man committing any of the following acts—\n(i). physical contact and advances involving unwelcome and explicit sexual overtures; or\n(ii). a demand or request for sexual favours; or\n(iii). showing pornography against the will of a woman; or\n(iv). making sexually coloured remarks, shall be guilty of the offence of sexual harassment.\nAny man who commits the offence specified in clause (i) or clause (ii) or clause (iii) of sub-section (1) shall be punished with rigorous imprisonment for a term which may extend to three years, or with fine, or with both.\nAny man who commits the offence specified in clause (iv) of sub-section (1) shall be punished with imprisonment of either description for a term which may extend to one year, or with fine, or with both.",
            "Whoever causes permanent or partial damage or deformity to, or bums or maims or disfigures or disables, any part or parts of the body of a person or causes grievous hurt by throwing acid on or by administering acid to that person, or by using any other means with the intention of causing or with the knowledge that he is likely to cause such injury or hurt, shall be punished with imprisonment of either description for a term which shall not be less than ten years but which may extend to imprisonment for life, and with fine.Provided that such fine shall be just and reasonable to meet the medical expenses of the treatment of the victim.Provided further that any fine imposed under this section shall be paid to the victim.\n",
            "Whoever does any act so rashly or negligently as to endanger human life or the personal safety others, shall be punished with imprisonment of either description for a term which may extend to three months or with fine which may extend to two hundred and fifty rupees, or with both.\n",
            "Whoever intentionally uses force to any person, without that person’ consent, in order to the committing of any offence, or intending by the use of such force to cause, or knowing it to be likely that by the use of such force he will cause injury, fear or annoyance to the person to whom the force is used, is said to use criminal force to that other.\n",
            "Whoever assaults or uses criminal force to any woman, intending to outrage or knowing it to be likely that he will there by outrage her modesty, shall be punished with imprisonment of either description for a term which shall not be less than one year but which may extend to five years, and shall also be liable to fine\n",
            "Any man who—\n(i) follows a woman and contacts, or attempts to contact such woman to foster personal interaction repeatedly despite a clear indication of disinterest by such woman; or\n(ii) monitors the use by a woman of the internet, email or any other form of electronic communication,commits the offence of stalking\n",
            "Whoever kidnaps or abducts any woman with intent that she may be compelled, or knowing it to be likely that she will be compelled, to marry any person against her will, or in order that she may be forced or seduced to illicit intercourse, or knowing it to be likely that she will be forced or seduced to illicit intercourse, shall be punished with imprisonment of either description for a term which may extend to ten years, and shall also be liable to fine; and whoever, by means of criminal intimidation as defined in this Code or of abuse of authority or any other method of compulsion, induces any woman to go from any place with intent that she may be, or knowing that it is likely that she will be, forced or seduced to illicit intercourse with another person shall also be punishable as aforesaid.\n",
            "Every man who by deceit causes any woman who is not lawfully married to him to believe that she is lawfully married to him and to cohabit or have sexual intercourse with him in that belief, shall be punished with imprisonment of either description for a term which may extend to ten years, and shall also be liable to fine.\n",
            "Whoever, having a husband or wife living, marries in any case in which such marriage is void by reason of its taking place during the life of such husband or wife, shall be punished with imprisonment of either description for a term which may extend to seven years, and shall also be liable to fine.\n",
            "Whoever, being the husband or the relative of the husband of a woman, subjects such woman to cruelty shall be punished with imprisonment for a term which may extend to three years and shall also be liable to fine.\n",
            "To provide for maternity benefits including maternity leave, wages, bonus, nursing breaks, etc. To protect the dignity of motherhood and the dignity of a new person by providing for full and healthy maintainence of the women and her child at this important time when she is not working.\n",
            "The citizens, men and women, equally have the right to an adequate means of livelihood. There must be equal work for both men and women. No citizen shall, on ground of sex, be ineligible with respect to any employment or office under state. When the employer doesn't comply with the provisions of the act, he will be liable to pay fine of Rs 10000-20000, or be imprisoned for 3 months-1 year, or both. Any settlement or any agreement that is harmful to the employee is not allowed.\n",
            "Whoever, intending to insult the modesty of any woman, utters any word, makes any sound or gesture, or exhibits any object, intending that such word or sound shall be heard, or that such gesture or object shall be seen, by such woman, or intrudes upon the privacy of such woman, shall be punished with simple imprisonment for a term which may extend to three years, and also with fine.\n",
            "To prohibit indecent representation of women through advertisements or in publications, writings, paintings, figures or in any other maner and for matters connected therewith or incidental thereto. Any person who contravenes the provisions shall be punishable on first conviction with imprisonment of either description for a term which may extend to two years, and with fine which may extend to two thousand rupees, and in the event of a second or subsequent conviction with imprisonment for term of not less than six months but which may extend to five years and also with a fine not less than ten thousand rupees but which may extend to one lakh rupees."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_details);

        Bundle b =getIntent().getExtras();
        lawsViolated = b.getStringArray("laws");
        sizeOfLawsViolated = b.getInt("size");
        if(sizeOfLawsViolated == 0){
            Intent intent = new Intent(CrimeDetailsActivity.this, NoResultsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        incident = b.getString("incident");

        lawsViolationDescriptions = new String[16];
        for(int i=0; i<sizeOfLawsViolated; i++){
            mLawNames.add(lawsViolated[i]);
            int flag=0;
            for(int j=1; j<16; j++){
                if(lawsViolated[i].equals(crimeTitles[j])){
                    mLawDescriptions.add(crimeDescriptions[j]);
                    lawsViolationDescriptions[i] = crimeDescriptions[j];
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                mLawDescriptions.add(crimeDescriptions[0]);
                lawsViolationDescriptions[i] = crimeDescriptions[0];
            }
        }

        additionalCommentsButton = findViewById(R.id.additionalCommentsButton);
        additionalCommentsDoneButton = findViewById(R.id.additionaCommentsDoneButton);
        lodgeFIRButton = findViewById(R.id.lodgeFIRButton);
        additionalCommentsView = findViewById(R.id.additionalCommentsView);
        remainderView = findViewById(R.id.remainderView);
        additionalCommentsEditText = findViewById(R.id.additionalCommentsEditText);

        additionalCommentsView.setVisibility(View.GONE);

        additionalCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalCommentsView.setVisibility(View.VISIBLE);
                remainderView.setAlpha((float)0.3);
            }
        });
        additionalCommentsDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalComments = additionalCommentsEditText.getText().toString();
                additionalCommentsView.setVisibility(View.GONE);
                remainderView.setAlpha((float)1.0);
            }
        });
        lodgeFIRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String completeDescription = "Incident:"+incident+"\nAdditional Comments: "+additionalCommentsEditText.getText().toString();
                DBHelper.addCrime(SPHelper.getSP(CrimeDetailsActivity.this, "uid"+SPHelper.getSP(CrimeDetailsActivity.this, "totalUsers")), completeDescription, "Kumaraswamy Layout Police Station");

                Intent intent = new Intent(CrimeDetailsActivity.this, FIRReportActivity.class);
                Bundle b=new Bundle();
                b.putStringArrayList("laws", mLawNames);
                b.putStringArrayList("descriptions", mLawDescriptions);
                String s = additionalCommentsEditText.getText().toString();
                if(s.isEmpty())
                b.putString("comments","none" );
                else
                    b.putString("comments",s);
                b.putString("incident", incident);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        mLawsView = (RecyclerView) findViewById(R.id.laws_view);
        mLawsView.setHasFixedSize(true);
        mLawsLayoutManager = new LinearLayoutManager(this);
        mLawsView.setLayoutManager(mLawsLayoutManager);
        mLawsAdapter =  new LawsAdapter(mLawNames, mLawDescriptions);
        mLawsView.setAdapter(mLawsAdapter);
        /*String display = "";
        for(int i=0; i<mLawNames.size(); i++){
            display = display+"\n"+mLawNames.get(i)+":"+mLawDescriptions.get(i);
        }*/
        //Toast.makeText(CrimeDetailsActivity.this, mLawDescriptions.get(1), Toast.LENGTH_SHORT).show();
    }
}
