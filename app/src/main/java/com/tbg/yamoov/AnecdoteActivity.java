package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AnecdoteActivity extends AppCompatActivity {

    private List<AnecdoteList> dataList = new ArrayList<>();
    private int redColor, greenColor;
    private RecyclerView.Adapter adapter;

    private Handler handler;
    private int currentPage = 0;
    private SwipeRefreshLayout refreshLayout;

    View positionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anecdote);

         positionView =(View)findViewById(R.id.main_position_view);

        boolean immerse = Utils.immerseStatusBar(this);
        boolean darkMode = Utils.setDarkMode(this);

        if (immerse) {
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = Utils.getStatusBarHeight(this);
            positionView.setLayoutParams(lp);
            if (!darkMode) {
                positionView.setBackgroundColor(Color.BLACK);
            }
        } else {
            positionView.setVisibility(View.GONE);
        }

        // 2. toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 3. recyclerView数据填充
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new VegaLayoutManager());
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        redColor = getResources().getColor(R.color.red);
        greenColor = getResources().getColor(R.color.green);

        appendDataList();
        adapter.notifyDataSetChanged();

        // 4. refreshLayout
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                requestHttp();
            }
        });
    }

    private void requestHttp() {
        if (null == handler) {
            handler = new Handler();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                appendDataList();
                adapter.notifyDataSetChanged();
            }
        }, 900);
    }

    private void appendDataList() {
        if (currentPage == 0) {
            dataList.clear();
        }
        currentPage++;

        dataList.add(new AnecdoteList("Yaoundé", "On dit souvent que Yaoundé est la ville des ngomna*. On les reconnaît rapidement aux costumes 7 pièces à la poste centrale vers 15h. Si un doute plane sur le fait qu’ils sont des agents de l’administration publique, la confirmation vient quand tu entends “100 frs Mendong!” ; rien à faire c’est un gars de Yaoundé plein !", "10/01/2019"));
        dataList.add(new AnecdoteList("Yaoundé", "A Yaoundé, quand le nkukuma veut faire pipi, on bloque les routes de 27 quartiers minimum. Et si vous voulez revendiquer, on cut même le courant une fois.", "10/01/2019"));
        dataList.add(new AnecdoteList("Douala", "A Yaoundé, tout le monde a un peu peur de tout le monde, on ne sait jamais ! C’est peut-être le père ou l’oncle, voire même le voisin d’un “haut placé” . Au moindre accrochage, tu peux entendre : “Tu me connais ? Tu sais qui je suis ?” ; question à laquelle les gars de Douala répondent souvent “C’est ta photo qui est sur l’argent ?“",  "10/01/2019"));
        dataList.add(new AnecdoteList("Yaoundé", "Si tu as grandi ou fréquenté les kwats comme Anguissa, Nkoldongo, Ntaba, Ngoa-Ekele ou encore Mvog-Ada, tu as sûrement rencontré au moins une fois dans ta vie un awasheur. C’est le grand du kwat toujours nerveux qu’on soupçonne de braquage la nuit mais qui en fait passe sa journée à taxer les munas. Le jour où il n’arrache pas ta belle paire de basket, le gars te donne 100 fcfa pour buy un pain chargé chocolat-beurre avec une bouteille de jus sur ça. En même temps, il calcule toujours les munas for tété, parce que s’il tente ça avec un bon petit du ghetto, lui même il va confirmer  !", "10/01/2019"));
        dataList.add(new AnecdoteList("Douala", "Si ton goût, c’est la Kadji Beer, tu vas seulement mourir de soif à Yaoundé. Ou le Bami là a écrit son nom sur la bière là pourquoi oohh ? Il se croit plus grand que qui ? (rires). Non mais plus sérieusement, un mbom de Yaoundé doit speak au moins quelques mots d’Ewondo et koch all les autres villes du pays. Un point c’est tout, la Kadji Beer ne va pas y échapper !",  "10/01/2019"));
        dataList.add(new AnecdoteList("Buea", "Tu vis à Yaoundé et tu ne know pas le pain-porc de Bastos ? Change de ville ! C’est l’endroit le plus high de la ville. Que tu sois Muna Bobo ou Nanga, tu as ta place là-bas. Baguette de pain avec brochette de porc suintant sur la braise, un peu de piment, de la mayonnaise et quelques gouttes d’arôme Maggi translucide façon façon. Et pour accompagner le tout, une carafe de jus naturel chez le vendeur d’à coté. Aaaassshhhhhh Yaoundé, ma ding wa !*",  "10/01/2019"));
        dataList.add(new AnecdoteList("Bertoua.", "Si tu vis à Mendong, Biteng, Damas… bref les quartiers de la zone là, 18h ne doit surtout pas te trouver à la poste, sinon c’est à 22h qu’un taximan qui rentre chez lui aura la gentillesse de te porter, non pas pour l’insignifiant tarif que tu as proposé, mais pour que tu lui tiennes compagnie. Heureusement pour toi qu’il habite ton quartier et qu’il aime les divers. Mais s’ils sont trois dans le taxi avec des têtes bizarres hein, mollah mieux tu waka ta chose, sinon c’est au lac municipal qu’on va te retrouver après le dépouillement …", "10/01/2019"));
        dataList.add(new AnecdoteList("Yaoundé", "A Yaoundé, ne stoppe jamais le taxi à partir de 22h pour aller à l’hôtel de ville. On sait tous ce que tu vas chercher là bas ! Ne discute même pas!",  "10/01/2019"));
        dataList.add(new AnecdoteList("Bafoussam.", "Tu vis à Yaoundé et tu n’aimes pas le Bikutsi ? Mon frère, même si on te lave au #auletch 45 fois et demi, ça ne va rien changer ! Tu seras toujours dans le ndem ! Le Bikutsi et Yaoundé c’est une affaire très sérieuse, c’est au coeur de son identité culturelle et ce n’est pas pour nous déplaire. ♫♫ “Ô pédalé, ô picasso”♫ – ♫ “Le gars là n’est pas marié mais il est déjà polygame, donne ma part sur ça” ♫♫",  "10/01/2019"));
        dataList.add(new AnecdoteList("Douala", "A Yaoundé, tout le monde a un peu peur de tout le monde, on ne sait jamais ! C’est peut-être le père ou l’oncle, voire même le voisin d’un “haut placé” . Au moindre accrochage, tu peux entendre : “Tu me connais ? Tu sais qui je suis ?” ; question à laquelle les gars de Douala répondent souvent “C’est ta photo qui est sur l’argent ?“",  "10/01/2019"));
        dataList.add(new AnecdoteList("Bafang", "On dit souvent que Yaoundé est la ville des ngomna*. On les reconnaît rapidement aux costumes 7 pièces à la poste centrale vers 15h. Si un doute plane sur le fait qu’ils sont des agents de l’administration publique, la confirmation vient quand tu entends “100 frs Mendong!” ; rien à faire c’est un gars de Yaoundé plein !",  "10/01/2019"));
        dataList.add(new AnecdoteList("Yaounde", "Si tu dois te rendre au marché Mokolo ou même à l’Avenue Kennedy, fais gaffe. Tu vas certainement croiser les agrégés de la lame de rasoir. Les gars te font subir une opération chirurgicale de ton sac à main ou de la poche arrière de ton jean, avec anesthésie de tout ton corps. Tu ne te rends compte de rien du tout. C’est peut être le lendemain que tu constates les dégâts. Et si tu repasses dans le même coin,il y a 60% de chance qu’on te vende le téléphone qu’on t’avait soutiré la veille.", "10/01/2019"));
        dataList.add(new AnecdoteList("Garoua", "On dit souvent que Yaoundé est la ville des ngomna*. On les reconnaît rapidement aux costumes 7 pièces à la poste centrale vers 15h. Si un doute plane sur le fait qu’ils sont des agents de l’administration publique, la confirmation vient quand tu entends “100 frs Mendong!” ; rien à faire c’est un gars de Yaoundé plein !",  "10/01/2019"));
        dataList.add(new AnecdoteList("Bertoua", "A Yaoundé, quand le nkukuma veut faire pipi, on bloque les routes de 27 quartiers minimum. Et si vous voulez revendiquer, on cut même le courant une fois.", "10/01/2019"));
        dataList.add(new AnecdoteList("Limbé", "A Yaoundé, tout le monde a un peu peur de tout le monde, on ne sait jamais ! C’est peut-être le père ou l’oncle, voire même le voisin d’un “haut placé” . Au moindre accrochage, tu peux entendre : “Tu me connais ? Tu sais qui je suis ?” ; question à laquelle les gars de Douala répondent souvent “C’est ta photo qui est sur l’argent ?“",  "10/01/2019"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                dataList.remove(0);
                adapter.notifyItemRemoved(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.item_anecdote, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MyViewHolder myHolder = (MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));

                if (position == dataList.size() - 1) {
                    requestHttp();
                }
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        };
        return adapter;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView anec;
    TextView date;

    public MyViewHolder(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.anec = (TextView) itemView.findViewById(R.id.item_anec);
        this.date = (TextView) itemView.findViewById(R.id.item_date);
    }

    public void bindData(AnecdoteList AnecdoteList) {
        title.setText(AnecdoteList.getTitle());
        anec.setText(AnecdoteList.getAnec());
        date.setText(AnecdoteList.getDate());
    }
}

