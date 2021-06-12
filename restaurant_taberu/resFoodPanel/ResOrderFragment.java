package com.example.restaurant_taberu.resFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_taberu.AddressRecycleView;
import com.example.restaurant_taberu.ClienteService;
import com.example.restaurant_taberu.R;
import com.example.restaurant_taberu.UserAddressActivity;
import com.example.restaurant_taberu.AddressDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ResOrderFragment extends Fragment {


    private Button addAddress;
    private Context context;
    private RecyclerView direcciones,mpago,rcupones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_user,null);

        setText(v);

        context = container.getContext();
        addAddress = (Button) v.findViewById(R.id.button);

        direcciones = v.findViewById(R.id.addressRecycleView);
        direcciones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        new LoadAddress().execute();


        addAdd(v);
        return v;
    }

    @Override
    public void onResume() {
       new LoadAddress().execute();


        super.onResume();
    }

    public void addAdd(View view)
    {


        addAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, UserAddressActivity.class);
                startActivity(intent);
            }
        });
    }





    public void setText(View view)
    {
        TextView textView = (TextView) view.findViewById(R.id.userNameTitle);
        textView.setText(Restaurante.getInstance().getNombre());

        TextView textView02 = (TextView) view.findViewById(R.id.textView17);
        textView02.setText(Restaurante.getInstance().getNombre());

        TextView textView03 = (TextView) view.findViewById(R.id.textView19);
        textView03.setText(Restaurante.getInstance().getEmail());

        TextView textView04 = (TextView) view.findViewById(R.id.textView21);
        textView04.setText(Restaurante.getInstance().getTelefono());

        TextView textView05 = (TextView) view.findViewById(R.id.textView12);
        textView05.setText(String.valueOf(Restaurante.getInstance().getPuntuacion()) + " Puntuación");
    }

    private class LoadAddress extends AsyncTask<Void, Void, Boolean> {

        ArrayList<AddressDetails> addressArrayList;

        public LoadAddress()

        {

        }

        @Override //Crea nuevo hilo
        protected Boolean doInBackground(Void... voids) {

            try {
                addressArrayList = ClienteService.getInstance().getAddress().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override//Regresa hilo principal
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result)
            {
                AddressRecycleView addressRecycleView = new AddressRecycleView(addressArrayList, false);
                direcciones.setAdapter(addressRecycleView);
            }
        }
    }










}