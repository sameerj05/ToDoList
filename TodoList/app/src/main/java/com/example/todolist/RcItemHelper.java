package com.example.todolist;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RcItemHelper extends ItemTouchHelper.SimpleCallback {



        private ListAdapter lstadapter;
        private ViewGroup container;
        private  LayoutInflater  inflater;
        public Activity activity;
        public Context context;
        public RcItemHelper(ListAdapter adapter, ViewGroup cont,  LayoutInflater  infl, Activity activity, Context context) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.lstadapter = adapter;
            this.container = cont;
            this.inflater=infl;
            this.context= context;
        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
           final int position = viewHolder.getAbsoluteAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this Task?");
                builder.setPositiveButton("Delete Task",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lstadapter.deleteTsk(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        lstadapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                lstadapter.editTsk(position);

            }
            lstadapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            View rootView =
                    inflater.inflate(R.layout.fragment_task_list, container, true);

            if (rootView == null)
                System.out.println("rootview is null");
//            ImageView imageView;

            Drawable icon;
            ColorDrawable background;

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;

            if (dX > 0) {
                icon = ContextCompat.getDrawable (rootView.getContext(),R.drawable.ic_baseline_edit);
 /*               imageView = (ImageView) rootView.findViewById(R.id.icon_edit);
                if (imageView == null)
                    System.out.println("IMAGEVIEW is null");
        //         imageView.setImageDrawable(ContextCompat.getDrawable(lstadapter.getContext(), R.drawable.ic_baseline_edit));
                icon = imageView.getDrawable(); */
                background = new ColorDrawable(Color.GREEN);
            } else {
                icon = ContextCompat.getDrawable (rootView.getContext(),R.drawable.ic_baseline_delete);
/*                imageView = (ImageView) rootView.findViewById(R.id.icon_delete);
                if (imageView == null)
                    System.out.println("IMAGEVIEW is null");
    //            imageView.setImageDrawable               (R.drawable.ic_baseline_delete));
                icon = imageView.getDrawable();*/
                background = new ColorDrawable(Color.RED);
            }

            assert icon != null;
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
        }


}
