package com.matterhornlegal.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.FirmGridItemBinding;
import com.matterhornlegal.databinding.FirmListItemBinding;
import com.matterhornlegal.models.FirmModel;
import com.matterhornlegal.models.LawFirmData;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan.kalsi on 10/27/2017.
 */

public class FirmListAdapter extends RecyclerView.Adapter<FirmListAdapter.ViewHolder> {

    private List<LawFirmData> firmList = new ArrayList<>();
    private FirmListAdapterListener listener;
    private AppConstants.VIEW_TYPE viewType = AppConstants.VIEW_TYPE.GRID;


    public FirmListAdapter(List<LawFirmData> firmList, FirmListAdapterListener listener) {
        this.firmList.clear();
        this.firmList.addAll(firmList);
        this.listener = listener;
    }

    public void setFirmList(List<LawFirmData> firmList) {
        this.firmList.clear();
        this.firmList.addAll(firmList);
        notifyDataSetChanged();
    }

    public List<LawFirmData> getFirmList() {
        return firmList;
    }

    @Override
    public FirmListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
       switch (this.viewType)
       {
           case GRID:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firm_grid_item, parent, false);
               break;
           case LIST:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firm_list_item, parent, false);
               break;
           default:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firm_grid_item, parent, false);
       }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }
    @Override
    public int getItemCount() {
        return firmList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /*TextView firm_title;
        ImageView firm_thumb;
        TextView firm_location;
        TextView firm_prac_areas;
        TextView firm_more_prac_areas;*/
        FirmListItemBinding firmListItemBinding;
        FirmGridItemBinding firmGridItemBinding;
        public ViewHolder(View itemView) {
            super(itemView);
            switch (viewType){
                case GRID:
                    firmGridItemBinding = DataBindingUtil.bind(itemView);
                    break;
                case LIST:
                    firmListItemBinding = DataBindingUtil.bind(itemView);
                    break;
            }

           /* firm_thumb = (ImageView) itemView.findViewById(R.id.firm_thumb);
            firm_title = (TextView) itemView.findViewById(R.id.firm_title);
            firm_location = (TextView) itemView.findViewById(R.id.firm_location);
            firm_prac_areas = (TextView) itemView.findViewById(R.id.firm_prac_areas);
            firm_more_prac_areas = (TextView) itemView.findViewById(R.id.firm_more_prac_areas);*/
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            String[] practiceAreaList = firmList.get(position).getPractice_area();

            switch (viewType){
                case GRID:
                    firmGridItemBinding.firmTitle.setText(firmList.get(position).getTitle());
                    Glide.with(itemView)
                            .load(firmList.get(position).getImage())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(firmGridItemBinding.firmThumb);
                    firmGridItemBinding.firmLocation.setText(firmList.get(position).getLocation());
                    if (practiceAreaList != null && practiceAreaList.length > 0){
                        firmGridItemBinding.txtNoPracticeArea.setVisibility(View.GONE);
                        firmGridItemBinding.lnrlayPracticeArea.setVisibility(View.VISIBLE);
                        if (practiceAreaList.length == 1){
                            firmGridItemBinding.firmPracAreas.setText(firmList.get(position).getPractice_area()[0]);
                            firmGridItemBinding.firmMorePracAreas.setVisibility(View.GONE);

                        }else{
                            firmGridItemBinding.firmPracAreas.setText(firmList.get(position).getPractice_area()[0]);
                            firmGridItemBinding.firmMorePracAreas.setVisibility(View.VISIBLE);
                            firmGridItemBinding.firmMorePracAreas.setText("+"+(firmList.get(position).getPractice_area().length -1));
                        }
                    }else{
                        firmGridItemBinding.txtNoPracticeArea.setVisibility(View.VISIBLE);
                        firmGridItemBinding.lnrlayPracticeArea.setVisibility(View.GONE);
                    }
                    break;
               case LIST:
                    firmListItemBinding.firmTitle.setText(firmList.get(position).getTitle());
                    Glide.with(itemView)
                            .load(firmList.get(position).getImage())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(firmListItemBinding.firmThumb);
                    firmListItemBinding.firmLocation.setText(firmList.get(position).getLocation());
                    if (practiceAreaList != null && practiceAreaList.length > 0){
                        firmListItemBinding.txtNoPracticeArea.setVisibility(View.GONE);
                        firmListItemBinding.lnrlayPracticeArea.setVisibility(View.VISIBLE);
                        if (practiceAreaList.length == 1){
                            firmListItemBinding.firmPracAreas.setText(firmList.get(position).getPractice_area()[0]);
                            firmListItemBinding.firmMorePracAreas.setVisibility(View.GONE);

                        }else{
                            firmListItemBinding.firmPracAreas.setText(firmList.get(position).getPractice_area()[0]);
                            firmListItemBinding.firmMorePracAreas.setVisibility(View.VISIBLE);
                            firmListItemBinding.firmMorePracAreas.setText("+"+(firmList.get(position).getPractice_area().length -1));
                        }
                    }else{
                        firmListItemBinding.txtNoPracticeArea.setVisibility(View.VISIBLE);
                        firmListItemBinding.lnrlayPracticeArea.setVisibility(View.GONE);
                    }
                    break;
            }
            /*firm_prac_areas.setText(firmList.get(position).getPracticeAreas().get(0));
            firm_more_prac_areas.setText("+"+(firmList.get(position).getPracticeAreas().size()-1));*/
        }

        @Override
        public void onClick(View v) {
            switch (viewType){
                case GRID:
                    listener.onFirmClicked(firmGridItemBinding.firmThumb, getLayoutPosition());
                    break;
                case LIST:
                    listener.onFirmClicked(firmListItemBinding.firmThumb, getLayoutPosition());
                    break;
            }


        }
    }

    public AppConstants.VIEW_TYPE getViewType() {
        return viewType;
    }

    public void setViewType(AppConstants.VIEW_TYPE viewType) {
        this.viewType = viewType;
    }

    public interface FirmListAdapterListener{
        void onFirmClicked( ImageView imageView, int position);
    }

}
