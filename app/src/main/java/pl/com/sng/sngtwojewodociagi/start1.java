
/**
 * Created by PBronk on 12.01.2017.
 */
package pl.com.sng.sngtwojewodociagi;

        import android.content.res.Configuration;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;

        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.PopupMenu;

/**
 * Created by PBronk on 11.01.2017.
 */

public class start1 extends Fragment {
    View myView;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.start_layout, container, false);
        final ImageButton aktual,awarie,jakosc,zgloszenie;


        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            myView.setBackgroundResource (R.drawable.i1024);
        } else {
            myView.setBackgroundResource (R.drawable.i1136);
        }

        aktual = (ImageButton) myView.findViewById(R.id.butt_aktualnosci);
        context_style(aktual,"1");
        aktual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              int id = v.getId();
                context_view("1");


           //     getFragmentManager().beginTransaction()
            //            .replace(R.id.content_frame, new aktualnosci())
            ///            .commit();
            }
        });

        aktual.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), aktual);
                popup.getMenuInflater()
                        .inflate(R.menu.activity_main_drawer, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                     //   Toast.makeText(
                      ///          getContext(),
                      ///          "You Clicked : " + item.getTitle(),
                     ////           Toast.LENGTH_SHORT
                    ///    ).show();

                        DataHandler data12 = new DataHandler(getContext());
                        data12.open();
                        data12.updateTableStart("1",item.getTitle().toString());
                        data12.close();
                        aktual.setId(item.getItemId());
                        changeTo(aktual,item,(String) item.getTitle(),item.getItemId());
                        return true;
                    }
                });
                popup.show();
                return true;
            }

        });
        awarie = (ImageButton)myView.findViewById(R.id.butt_awarie);
        context_style(awarie,"2");
        awarie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                context_view("2");

             //   getFragmentManager().beginTransaction()
              //          .replace(R.id.content_frame, new wiadomosci())
              //          .commit();
            }
        });


        awarie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), awarie);
                popup.getMenuInflater()
                        .inflate(R.menu.activity_main_drawer, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        DataHandler data13 = new DataHandler(getContext());
                        data13.open();
                        data13.updateTableStart("2",item.getTitle().toString());
                        data13.close();

                        awarie.setId(item.getItemId());
                        changeTo(awarie,item,(String) item.getTitle(),item.getItemId());

                        return true;

                    }
                });
                popup.show();
                return true;
            }

        });


        jakosc = (ImageButton)myView.findViewById(R.id.butt_jakoscwody);
        context_style(jakosc,"3");

        jakosc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context_view("3");

            //    getFragmentManager().beginTransaction()
              //          .replace(R.id.content_frame, new JakoscDzielnice())
              //          .commit();
            }
        });

        jakosc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), jakosc);
                popup.getMenuInflater()
                        .inflate(R.menu.activity_main_drawer, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        DataHandler data14 = new DataHandler(getContext());
                        data14.open();
                        data14.updateTableStart("3",item.getTitle().toString());
                        data14.close();

                        jakosc.setId(item.getItemId());
                        changeTo(jakosc,item,(String) item.getTitle(),item.getItemId());

                        return true;

                    }
                });
                popup.show();
                return true;
            }

        });



        zgloszenie = (ImageButton)myView.findViewById(R.id.butt_zgloszenieawari);
        context_style(zgloszenie,"4");
        zgloszenie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                context_view("4");
           //     getFragmentManager().beginTransaction()
           //             .replace(R.id.content_frame, new zgloszenie_awarii())
            //            .commit();
            }
        });

        zgloszenie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), zgloszenie);
                popup.getMenuInflater()
                        .inflate(R.menu.activity_main_drawer, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        DataHandler data15 = new DataHandler(getContext());
                        data15.open();
                        data15.updateTableStart("4",item.getTitle().toString());
                        data15.close();
                        zgloszenie.setId(item.getItemId());
                        changeTo(zgloszenie,item,(String) item.getTitle(),item.getItemId());
                        return true;
                    }
                });
                popup.show();
                return true;
            }

        });
        return  myView;
    }

    private void context_view(String s) {

        DataHandler dh = new DataHandler(getContext());
        dh.open();
        Cursor tempC = dh.returnContect(s);
        tempC.moveToFirst();
        String aktualny_link = tempC.getString(tempC.getColumnIndex(DataHandler.START_NAZWA));
        dh.close();

        switch (aktualny_link) {
            case "O nas":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new about())
                        .commit();
                break;
            case "Aktualności":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new aktualnosci())
                        .commit();
                break;
            case "Awarie":

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new wiadomosci())
                        .commit();
                break;
            case "Jakość wody":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new JakoscDzielnice())
                        .commit();
                break;
            case "Kąpieliska morskie":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new kapieliska())
                        .commit();
                break;
            case "Planowe wyłączenienia wody":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new wylaczeniaPlanowe())
                        .commit();
                break;
            case "Praca w SNG":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PracaSNG())
                        .commit();
                break;
            case "Cennik":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new cennik1())
                        .commit();
                break;
            case "Zgłoszenie awarii":
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new zgloszenie_awarii())
                        .commit();
                break;
            case "Ustawienia":

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new settings())
                        .commit();
                break;

        }

    }

    private void context_style(ImageButton button,String s) {

        DataHandler dh = new DataHandler(getContext());
        dh.open();
       Cursor tempC = dh.returnContect(s);
        tempC.moveToFirst();
        String aktualny_link = tempC.getString(tempC.getColumnIndex(DataHandler.START_NAZWA));
                dh.close();

        switch(aktualny_link){
            case "O nas":
                button.setImageResource(R.drawable.icone_opai);
            //    button.setBackgroundColor(Color.YELLOW);//
                button.setBackgroundResource(R.drawable.button_ona);
                break;
            case "Aktualności":
                button.setBackgroundResource(R.drawable.button_aktual);
                button.setImageResource(R.drawable.icone_aktualnosci);
            //    button.setBackgroundColor(Color.BLUE);//
                break;
            case "Awarie":
                button.setBackgroundResource(R.drawable.button_awar);
              //  button.setBackgroundColor(Color.RED);//
                button.setImageResource(R.drawable.icone_awarie);
                break;
            case "Jakość wody":
                button.setImageResource(R.drawable.icone_jakosc);
              //  button.setBackgroundColor(Color.GREEN);//
                button.setBackgroundResource(R.drawable.button_jako);
                break;
            case "Kąpieliska morskie":
                button.setImageResource(R.drawable.icone_kapieliska);
              //  button.setBackgroundColor(Color.MAGENTA);//
                button.setBackgroundResource(R.drawable.button_kapie);
                break;
            case "Planowe wyłączenienia wody":
                button.setImageResource(R.drawable.icone_planowewyl);
              //  button.setBackgroundColor(Color.GRAY);//
                button.setBackgroundResource(R.drawable.button_plan);
                break;
            case "Cennik":
                button.setImageResource(R.drawable.icon_cennik);
            //    button.setBackgroundColor(Color.YELLOW);//
                button.setBackgroundResource(R.drawable.button_cenn);
                break;
            case "Zgłoszenie awarii":
                button.setImageResource(R.drawable.icone_zgloszenieawari);
                // button.setBackgroundColor(Color.CYAN);//
                button.setBackgroundResource(R.drawable.button_zgloz);
                break;
            case "Ustawienia":

                button.setImageResource(R.drawable.icone_ustawienia);
                button.setBackgroundColor(Color.LTGRAY);//
                break;

        }


    }

    private void changeTo(ImageButton button, MenuItem item, String title, int itemId) {


        switch (item.getItemId() ){
            case R.id.about:
                button.setImageResource(R.drawable.icone_opai);
                //    button.setBackgroundColor(Color.YELLOW);//
                button.setBackgroundResource(R.drawable.button_ona);


                break;
            case R.id.aktualnosci:
                button.setBackgroundResource(R.drawable.button_aktual);
                button.setImageResource(R.drawable.icone_aktualnosci);
                //    button.setBackgroundColor(Color.BLUE);//
                break;
            case R.id.wiadomosci:
                button.setBackgroundResource(R.drawable.button_awar);
                //  button.setBackgroundColor(Color.RED);//
                button.setImageResource(R.drawable.icone_awarie);
                break;
            case R.id.kapieliska:
                button.setImageResource(R.drawable.icone_kapieliska);
                //  button.setBackgroundColor(Color.MAGENTA);//
                button.setBackgroundResource(R.drawable.button_kapie);
                break;
            case R.id.wylaczenia_planowe:
                button.setImageResource(R.drawable.icone_planowewyl);
                //  button.setBackgroundColor(Color.GRAY);//
                button.setBackgroundResource(R.drawable.button_plan);
                break;
            case R.id.cennik:
                button.setImageResource(R.drawable.icon_cennik);
                //    button.setBackgroundColor(Color.YELLOW);//
                button.setBackgroundResource(R.drawable.button_cenn);
                break;
            case R.id.zgloszenie_awarii:
                button.setImageResource(R.drawable.icone_zgloszenieawari);
                // button.setBackgroundColor(Color.CYAN);//
                button.setBackgroundResource(R.drawable.button_zgloz);
                break;
           // case R.id.ustawienia:
           //     button.setImageResource(R.drawable.icone_ustawienia);
           //     button.setBackgroundColor(Color.LTGRAY);//
           //     break;
            case R.id.jakosc_wody:
                button.setImageResource(R.drawable.icone_jakosc);
                //  button.setBackgroundColor(Color.GREEN);//
                button.setBackgroundResource(R.drawable.button_jako);
                break;

        }
    }
}
