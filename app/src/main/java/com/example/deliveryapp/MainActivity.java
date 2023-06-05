package com.example.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mapkit.MapKitFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //курьер
    Courier courier = new Courier("Калмацкий Никита Михайлович", "894560790457");

    //посылки
    SmallPackage vase = new SmallPackage("10*10*40", true, "Томск", "Москва");
    SmallPackage bag = new SmallPackage("30*40*50", false, "Омск", "Сочи");
    Documents docs = new Documents("Москва", "Омск");
    BigPackage babyCarriage = new BigPackage("100*70*80", false, 10, "Казань", "Москва");

    //компании
    Company yandex = new Company("Yandex", "Москва, ул. Льва Толстого, 16");
    Company google = new Company("Google", "America");

    //заказы
    Order order1 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order2 = new Order(yandex, bag, bag.getFrom(), bag.getTo(), 1500);
    Order order3 = new Order(google, docs, docs.getFrom(), docs.getTo(), 1600);
    Order order4 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order5 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order6 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order7 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order8 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order9 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    Order order10 = new Order(yandex, vase, vase.getFrom(), vase.getTo(), 1200);
    ArrayList<Order> orders = new ArrayList(Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10));

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("3e9ed211-3558-476a-ab52-9b29735e3a9e");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        TextView courier_name = findViewById(R.id.courier_name);
        courier_name.setText(courier.getFIO());

        courier.setFeatures("Личный автомобиль");

        TextView features = findViewById(R.id.couruier_features);
        features.setText(courier.getFeatures());

        TextView courier_acc = findViewById(R.id.courier_acc);
        courier_acc.setText(courier.getCheckingAccount());

        courier.addOrder(order1);
        courier.addOrder(order2);
        courier.addOrder(order3);
        courier.addOrder(order4);
        courier.addOrder(order5);
        courier.addOrder(order6);
        courier.addOrder(order7);
        courier.addOrder(order8);
        courier.addOrder(order9);
        courier.addOrder(order10);


        listView = findViewById(R.id.listView);

        OrderAdapter adapter = new OrderAdapter(this, courier.getOrders());


        listView.setAdapter(adapter);

        Button btn_ok = findViewById(R.id.button_ok);
        Button btn_clear = findViewById(R.id.button_clear);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mapAct(orders.get(position).getAddrTo());
                return false;
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double result = 0;
                for (int i = 0; i < adapter.getOrders().size(); i++) {
                     if (adapter.getOrders().get(i).isSelected()) {
                         result += Double.parseDouble(adapter.getOrders().get(i).getCost());
                     }
                }
                showInfo(result);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < adapter.getOrders().size(); i++) {
                     adapter.getOrders().get(i).setSelected(false);
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void showInfo(double cost) {
        Toast.makeText(this, "Общая стоимость: " + cost, Toast.LENGTH_LONG).show();
    }

    public void mapAct(String address) {
        Intent intent = new Intent(this, Address.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }

}