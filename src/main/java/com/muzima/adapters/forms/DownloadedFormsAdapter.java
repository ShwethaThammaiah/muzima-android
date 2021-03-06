/*
 * Copyright (c) 2014. The Trustees of Indiana University.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license with additional
 * healthcare disclaimer. If the user is an entity intending to commercialize any application
 * that uses this code in a for-profit venture, please contact the copyright holder.
 */

/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.muzima.adapters.forms;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.muzima.R;
import com.muzima.controller.FormController;
import com.muzima.model.DownloadedForm;
import com.muzima.model.collections.DownloadedForms;
import com.muzima.tasks.FormsAdapterBackgroundQueryTask;

import java.util.List;

/**
 * Responsible to list all the downloaded forms.
 */
public class DownloadedFormsAdapter extends FormsAdapter<DownloadedForm> {
    private static final String TAG = "DownloadedFormsAdapter";

    public DownloadedFormsAdapter(Context context, int textViewResourceId, FormController formController) {
        super(context, textViewResourceId, formController);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

        hideTagScroller(convertView);
        return convertView;
    }

    private void hideTagScroller(View convertView) {
        convertView.findViewById(R.id.tags_scroller).setVisibility(View.GONE);
    }

    public void clearSelectedForms() {
        notifyDataSetChanged();
    }

    @Override
    protected int getFormItemLayout() {
        return R.layout.item_forms_list_selectable;
    }

    @Override
    public void reloadData() {
        new BackgroundQueryTask(this).execute();
    }

    /**
     * Responsible to fetch all the
     */
    public class BackgroundQueryTask extends FormsAdapterBackgroundQueryTask<DownloadedForm> {

        public BackgroundQueryTask(FormsAdapter formsAdapter) {
            super(formsAdapter);
        }

        @Override
        protected List<DownloadedForm> doInBackground(Void... params) {
            DownloadedForms downloadedForms = null;
            if (adapterWeakReference.get() != null) {
                try {
                    FormsAdapter formsAdapter = adapterWeakReference.get();
                    downloadedForms = formsAdapter.getFormController().getAllDownloadedForms();
                    Log.i(TAG, "#Forms with templates: " + downloadedForms.size());
                } catch (FormController.FormFetchException e) {
                    Log.w(TAG, "Exception occurred while fetching local forms ", e);
                }
            }
            return downloadedForms;
        }
    }
}
