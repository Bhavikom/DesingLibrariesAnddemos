package com.yasoka.eazyscreenrecord.alertdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.utils.PreferenceHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class SelectResolutionDialogFragment extends DialogFragment {
    private static final String KEY_RESOLUTION_PREFERENCE = "key_resolution_type_arg";
    private TextView cancelTxt;
    private ImageView closeButton;
    private ListView listView;
    private TextView okTxt;
    /* access modifiers changed from: private */
    public int preferenceType = -1;
    /* access modifiers changed from: private */
    public String[] resolutionArray;
    /* access modifiers changed from: private */
    public int selectedPosition;
    private TextView title;

    @Retention(RetentionPolicy.SOURCE)
    @interface ResolutionType {
    }

    public static SelectResolutionDialogFragment getInstance(int i) {
        SelectResolutionDialogFragment selectResolutionDialogFragment = new SelectResolutionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_RESOLUTION_PREFERENCE, i);
        selectResolutionDialogFragment.setArguments(bundle);
        return selectResolutionDialogFragment;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (getActivity() instanceof OnDismissListener) {
            ((OnDismissListener) getActivity()).onDismiss(dialogInterface);
        }
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            this.preferenceType = getArguments().getInt(KEY_RESOLUTION_PREFERENCE, -1);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_settings_list_dialog, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.listView = (ListView) view.findViewById(C0793R.C0795id.list_items);
        this.title = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_title);
        this.okTxt = (TextView) view.findViewById(C0793R.C0795id.txt_ok);
        this.cancelTxt = (TextView) view.findViewById(C0793R.C0795id.txt_cancel);
        this.closeButton = (ImageView) view.findViewById(C0793R.C0795id.img_close_dialog);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        boolean z;
        super.onActivityCreated(bundle);
        this.resolutionArray = getResources().getStringArray(C0793R.array.pref_resolution_list_titles);
        this.title.setText(C0793R.string.resolution);
        this.title.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_resolution, 0, 0, 0);
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);
        int i = point.y;
        int i2 = point.x;
        ArrayList arrayList = new ArrayList();
        String[] strArr = this.resolutionArray;
        int length = strArr.length;
        int i3 = 0;
        while (true) {
            z = true;
            if (i3 >= length) {
                break;
            }
            String str = strArr[i3];
            String str2 = "x";
            String str3 = str.split(str2)[0];
            String str4 = str.split(str2)[1];
            if (i > i2) {
                if (Integer.parseInt(str3) <= i && Integer.parseInt(str4) <= i2) {
                    arrayList.add(str);
                }
            } else if (Integer.parseInt(str4) <= i && Integer.parseInt(str3) <= i2) {
                arrayList.add(str);
            }
            i3++;
        }
        this.resolutionArray = (String[]) arrayList.toArray(new String[arrayList.size()]);
        if (PreferenceHelper.getInstance().hasPrefResolution(this.preferenceType)) {
            String prefResolution = PreferenceHelper.getInstance().getPrefResolution(this.preferenceType);
            int i4 = 0;
            while (true) {
                String[] strArr2 = this.resolutionArray;
                if (i4 >= strArr2.length) {
                    break;
                } else if (strArr2[i4].startsWith(prefResolution)) {
                    this.selectedPosition = i4;
                    break;
                } else {
                    i4++;
                }
            }
            if (!z && this.resolutionArray.length > 0) {
                PreferenceHelper.getInstance().setPrefResolution(this.preferenceType, this.resolutionArray[0]);
            }
            this.listView.setAdapter(new ArrayAdapter<String>(getContext(), C0793R.layout.settings_dialog_list_items, this.resolutionArray) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = LayoutInflater.from(getContext()).inflate(C0793R.layout.settings_dialog_list_items, null);
                    }
                    RadioButton radioButton = (RadioButton) view.findViewById(C0793R.C0795id.radio_list_item);
                    radioButton.setChecked(i == SelectResolutionDialogFragment.this.selectedPosition);
                    radioButton.setTag(Integer.valueOf(i));
                    radioButton.setText(SelectResolutionDialogFragment.this.resolutionArray[i]);
                    radioButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SelectResolutionDialogFragment.this.selectedPosition = ((Integer) view.getTag()).intValue();
                            C10301.this.notifyDataSetChanged();
                        }
                    });
                    return view;
                }
            });
            this.okTxt.findViewById(C0793R.C0795id.txt_ok).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    PreferenceHelper.getInstance().setPrefResolution(SelectResolutionDialogFragment.this.preferenceType, SelectResolutionDialogFragment.this.resolutionArray[SelectResolutionDialogFragment.this.selectedPosition]);
                    SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
                }
            });
            this.cancelTxt.findViewById(C0793R.C0795id.txt_cancel).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
                }
            });
            this.closeButton.findViewById(C0793R.C0795id.img_close_dialog).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
                }
            });
        }
        z = false;
        PreferenceHelper.getInstance().setPrefResolution(this.preferenceType, this.resolutionArray[0]);
        this.listView.setAdapter(new ArrayAdapter<String>(getContext(), C0793R.layout.settings_dialog_list_items, this.resolutionArray) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(getContext()).inflate(C0793R.layout.settings_dialog_list_items, null);
                }
                RadioButton radioButton = (RadioButton) view.findViewById(C0793R.C0795id.radio_list_item);
                radioButton.setChecked(i == SelectResolutionDialogFragment.this.selectedPosition);
                radioButton.setTag(Integer.valueOf(i));
                radioButton.setText(SelectResolutionDialogFragment.this.resolutionArray[i]);
                radioButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        SelectResolutionDialogFragment.this.selectedPosition = ((Integer) view.getTag()).intValue();
                        C10301.this.notifyDataSetChanged();
                    }
                });
                return view;
            }
        });
        this.okTxt.findViewById(C0793R.C0795id.txt_ok).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PreferenceHelper.getInstance().setPrefResolution(SelectResolutionDialogFragment.this.preferenceType, SelectResolutionDialogFragment.this.resolutionArray[SelectResolutionDialogFragment.this.selectedPosition]);
                SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
            }
        });
        this.cancelTxt.findViewById(C0793R.C0795id.txt_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
            }
        });
        this.closeButton.findViewById(C0793R.C0795id.img_close_dialog).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SelectResolutionDialogFragment.this.dismissAllowingStateLoss();
            }
        });
    }
}
