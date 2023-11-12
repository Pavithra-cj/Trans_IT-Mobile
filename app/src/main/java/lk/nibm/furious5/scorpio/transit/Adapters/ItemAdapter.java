package lk.nibm.furious5.scorpio.transit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.nibm.furious5.scorpio.transit.Model.PackageItem;
import lk.nibm.furious5.scorpio.transit.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.itemViewHolder> {

    private Context iContext;
    private ArrayList<PackageItem> packageItemsList;

    public ItemAdapter(Context context, ArrayList<PackageItem> packageList){
        iContext = context;
        packageItemsList = packageList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(iContext).inflate(R.layout.item, parent, false);
        return new itemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        PackageItem currentItem = packageItemsList.get(position);

        String pkgName = currentItem.getiPkgName();
        String credits = currentItem.getiCreditPoints();
        String price = currentItem.getiPrice();

        holder.iPkgName.setText(pkgName);
        holder.iCreditPoints.setText(credits);
        holder.iPrice.setText("LKR " + price);

    }

    @Override
    public int getItemCount() {
        return packageItemsList.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder{
        public TextView iPkgName;
        public TextView iCreditPoints;
        public TextView iPrice;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            iPkgName = itemView.findViewById(R.id.txtPackageName);
            iCreditPoints = itemView.findViewById(R.id.txtCreditPoints);
            iPrice = itemView.findViewById(R.id.txtPrice);

        }
    }

}
