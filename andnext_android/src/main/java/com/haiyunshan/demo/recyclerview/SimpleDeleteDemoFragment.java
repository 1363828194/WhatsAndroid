package com.haiyunshan.demo.recyclerview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import club.andnext.recyclerview.bridge.BridgeAdapter;
import club.andnext.recyclerview.bridge.BridgeAdapterProvider;
import club.andnext.recyclerview.bridge.BridgeBuilder;
import club.andnext.recyclerview.bridge.BridgeHolder;
import club.andnext.recyclerview.decoration.MarginDividerDecoration;
import club.andnext.recyclerview.itemtouch.ItemSwipeHelper;
import club.andnext.recyclerview.swipe.SwipeActionHelper;
import club.andnext.recyclerview.swipe.SwipeHolder;
import club.andnext.recyclerview.swipe.SwipeViewHolder;
import club.andnext.recyclerview.swipe.runner.DeleteRunner;
import com.haiyunshan.dataset.PiliDataset;
import com.haiyunshan.whatsandroid.R;

public class SimpleDeleteDemoFragment extends BaseSimpleRVDemoFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        {
            ItemSwipeHelper helper = new ItemSwipeHelper(overScrollHelper);
            helper.attachToRecyclerView(recyclerView);
//            helper.setRightEnable(false);
//            helper.setLeftEnable(false);
        }
    }

    protected BridgeAdapter createAdapter() {
        this.adapter = new BridgeAdapter(getActivity(), new BridgeAdapterProvider<PiliDataset.PiliEntity>() {

            @Override
            public PiliDataset.PiliEntity get(int position) {
                return dataset.get(position);
            }

            @Override
            public int size() {
                return dataset.size();
            }
        });

        adapter.bind(PiliDataset.PiliEntity.class,
                new BridgeBuilder(DemoViewHolder.class, DemoViewHolder.LAYOUT_RES_ID, this));

        return adapter;
    }

    /**
     *
     */
    private static class DemoViewHolder extends BridgeHolder<PiliDataset.PiliEntity> implements View.OnClickListener, ItemSwipeHelper.Adapter, MarginDividerDecoration.Adapter {

        static final int LAYOUT_RES_ID = R.layout.layout_pili_simple_delete_list_item;

        SimpleDeleteDemoFragment parent;

        View contentView;

        TextView nameView;
        TextView poemView;

        DemoViewHolder(SimpleDeleteDemoFragment parent, View itemView) {
            super(itemView);

            this.parent = parent;
        }

        @Override
        public int getLayoutResourceId() {
            return LAYOUT_RES_ID;
        }

        @Override
        public void onViewCreated(@NonNull View view) {
            {
                this.contentView = view.findViewById(R.id.content_layout);

                this.nameView = view.findViewById(R.id.tv_name);
                this.poemView = view.findViewById(R.id.tv_poem);

                contentView.setOnClickListener(this);

            }
        }

        @Override
        public void onBind(PiliDataset.PiliEntity item, int position) {

            nameView.setText(item.getName());
            poemView.setText(item.getPoem());
        }

        @Override
        public void onClick(View v) {
            if (v == contentView) {
                this.click();
            }
        }

        @Override
        public boolean isEnable(ItemSwipeHelper delegate) {
            return true;
        }

        @Override
        public void onSwiped(ItemSwipeHelper delegate, int direction) {
            this.delete();
        }

        @Override
        public float getTranslation(MarginDividerDecoration decoration) {
            return itemView.getTranslationX();
        }

        void click() {
            int position = this.getAdapterPosition();
            PiliDataset.PiliEntity entity = parent.dataset.get(position);

            Snackbar.make(itemView, entity.getPoem(), Snackbar.LENGTH_LONG).show();

        }

        void delete() {
            int position = this.getAdapterPosition();
            parent.dataset.remove(position);
            parent.adapter.notifyItemRemoved(position);
        }
    }
}