package ui;

/**
 * Created by Informatica 2 on 25/5/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertController;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.informatica2.aguascomayagua.R;

public class adaptadorfacturacion extends AlertController.RecycleListView{
    // Campos respectivos de un item
    public TextView valor;
    public TextView nombre;
    public TextView fecha;


    public ExpenseViewHolder(View v) {
        super(v);
        valor = (TextView) v.findViewById(R.id.valor);
        nombre = (TextView) v.findViewById(R.id.nombre);
        fecha = (TextView) v.findViewById(R.id.fecha);

    }

    public AdaptadorDeGastos(Context context) {
        this.context= context;

    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout, viewGroup, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String valor;
        String nombre;
        String fecha;

        valor = cursor.getString(1);
        nombre = cursor.getString(2);
        fecha = cursor.getString(3);

        viewHolder.valor.setText("$"+valor);
        viewHolder.nombre.setText(nombre);
        viewHolder.fecha.setText(fecha);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
