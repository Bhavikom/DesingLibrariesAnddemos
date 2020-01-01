package com.yasoka.eazyscreenrecord.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p000v4.app.Fragment;*/
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.utils.Constants;
import com.ezscreenrecorder.utils.PreferenceHelper;
import com.ezscreenrecorder.utils.StorageHelper;
import com.facebook.appevents.AppEventsConstants;*/
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.utils.Constants;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;

import java.util.ArrayList;

public class SettingsListDialogFragment extends DialogFragment {
    /* access modifiers changed from: private */
    public TextView note;
    /* access modifiers changed from: private */
    public SharedPreferences prefs;
    /* access modifiers changed from: private */
    public int selectedPosition;
    /* access modifiers changed from: private */
    public int type;

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnDismissListener) {
            ((OnDismissListener) parentFragment).onDismiss(dialogInterface);
        } else if (getActivity() instanceof OnDismissListener) {
            ((OnDismissListener) getActivity()).onDismiss(dialogInterface);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.type = getArguments().getInt("type");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_settings_list_dialog, viewGroup, false);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        final String[] strArr;
        String[] strArr2;
        super.onViewCreated(view, bundle);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        ListView listView = (ListView) view.findViewById(C0793R.C0795id.list_items);
        TextView textView = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_title);
        this.note = (TextView) view.findViewById(C0793R.C0795id.txt_note_settings);
        TextView textView2 = (TextView) view.findViewById(C0793R.C0795id.txt_dialog_desc);
        CheckBox checkBox = (CheckBox) view.findViewById(C0793R.C0795id.chk_green);
        int i = this.type;
        boolean z = true;
        int i2 = 0;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    strArr = getResources().getStringArray(C0793R.array.pref_bit_rate_list_titles);
                    textView.setText(C0793R.string.bit_rate);
                    textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_bitrate, 0, 0, 0);
                    Float valueOf = Float.valueOf(Float.parseFloat(this.prefs.getString("example_list_bit_rate", String.valueOf(1000000))) / 1000000.0f);
                    String[] stringArray = getResources().getStringArray(C0793R.array.pref_bit_rate_list_titles);
                    while (true) {
                        if (i2 >= stringArray.length) {
                            break;
                        }
                        try {
                            if (Float.valueOf(Float.parseFloat(stringArray[i2].replace("Mbps", "").trim())).equals(valueOf)) {
                                this.selectedPosition = i2;
                                break;
                            }
                            i2++;
                        } catch (Exception unused) {
                            if (stringArray[i2].startsWith(String.valueOf(valueOf))) {
                                this.selectedPosition = i2;
                                break;
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 4) {
                        strArr = getResources().getStringArray(C0793R.array.pref_count_down_list_titles);
                        textView.setText(C0793R.string.count_down);
                        textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_count_down, 0, 0, 0);
                        Integer valueOf2 = Integer.valueOf(PreferenceHelper.getInstance().getPrefRecordingCountdown());
                        String[] stringArray2 = getResources().getStringArray(C0793R.array.pref_count_down_list_titles);
                        while (true) {
                            if (i2 < stringArray2.length) {
                                if (stringArray2[i2].equalsIgnoreCase("No CountDown") && valueOf2.intValue() == 0) {
                                    this.selectedPosition = i2;
                                    break;
                                } else if (stringArray2[i2].startsWith(String.valueOf(valueOf2))) {
                                    this.selectedPosition = i2;
                                    break;
                                } else {
                                    i2++;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if (i == 6) {
                        strArr = getResources().getStringArray(C0793R.array.pref_long_click_list_titles);
                        textView.setText(C0793R.string.floating_button_long_click);
                        textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_long_click, 0, 0, 0);
                        this.selectedPosition = Integer.valueOf(Integer.parseInt(this.prefs.getString("example_list_long_click", AppEventsConstants.EVENT_PARAM_VALUE_NO))).intValue();
                    } else if (i != 7) {
                        strArr2 = null;
                    } else {
                        strArr = getResources().getStringArray(C0793R.array.pref_storage_location);
                        textView.setText(C0793R.string.txt_storage_setting_title);
                        textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_default_storage_location, 0, 0, 0);
                        this.selectedPosition = PreferenceHelper.getInstance().getPrefDefaultStorageLocation();
                    }
                } else {
                    strArr = getResources().getStringArray(C0793R.array.pref_orientation_list_titles);
                    textView.setText(C0793R.string.orientation);
                    textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_orientation, 0, 0, 0);
                    String prefRecordingOrientation = PreferenceHelper.getInstance().getPrefRecordingOrientation();
                    String[] stringArray3 = getResources().getStringArray(C0793R.array.pref_orientation_list_values);
                    while (true) {
                        if (i2 >= stringArray3.length) {
                            break;
                        } else if (stringArray3[i2].startsWith(prefRecordingOrientation)) {
                            this.selectedPosition = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            } else {
                strArr = getResources().getStringArray(C0793R.array.pref_frame_rate_list_titles);
                textView.setText(C0793R.string.frame_rate);
                textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_frame_rate, 0, 0, 0);
                String string = this.prefs.getString("example_list_frame_rate", String.valueOf(30));
                String[] stringArray4 = getResources().getStringArray(C0793R.array.pref_frame_rate_list_titles);
                while (true) {
                    if (i2 >= stringArray4.length) {
                        break;
                    } else if (stringArray4[i2].startsWith(string)) {
                        this.selectedPosition = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
            }
            final String[] strArr3 = strArr;
            C14121 r5 = new ArrayAdapter<String>(getContext(), C0793R.layout.settings_dialog_list_items, strArr) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = LayoutInflater.from(getContext()).inflate(C0793R.layout.settings_dialog_list_items, null);
                    }
                    RadioButton radioButton = (RadioButton) view.findViewById(C0793R.C0795id.radio_list_item);
                    radioButton.setChecked(i == SettingsListDialogFragment.this.selectedPosition);
                    radioButton.setTag(Integer.valueOf(i));
                    if (SettingsListDialogFragment.this.type == 7) {
                        if (StorageHelper.getInstance().externalMemoryAvailable() || i != 1) {
                            radioButton.setEnabled(true);
                        } else {
                            radioButton.setEnabled(false);
                        }
                        String str = "%s (%s / %s)";
                        if (i == 0) {
                            radioButton.setText(String.format(str, new Object[]{strArr3[i], StorageHelper.getInstance().getAvailableInternalMemorySize(), StorageHelper.getInstance().getTotalInternalMemorySize()}));
                        } else if (i == 1) {
                            if (StorageHelper.getInstance().externalMemoryAvailable()) {
                                radioButton.setText(String.format(str, new Object[]{strArr3[i], StorageHelper.getInstance().getAvailableExternalMemorySize(), StorageHelper.getInstance().getTotalExternalMemorySize()}));
                            } else {
                                radioButton.setText(String.format("%s ( %s )", new Object[]{strArr3[i], SettingsListDialogFragment.this.getString(C0793R.string.id_not_available_txt)}));
                            }
                        }
                    } else {
                        radioButton.setText(strArr3[i]);
                    }
                    radioButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SettingsListDialogFragment.this.selectedPosition = ((Integer) view.getTag()).intValue();
                            if (SettingsListDialogFragment.this.type == 7) {
                                int access$000 = SettingsListDialogFragment.this.selectedPosition;
                                if (access$000 == 0) {
                                    SettingsListDialogFragment.this.note.setVisibility(4);
                                } else if (access$000 == 1) {
                                    SettingsListDialogFragment.this.note.setVisibility(0);
                                    SettingsListDialogFragment.this.note.setTextColor(-65536);
                                    SettingsListDialogFragment.this.note.setText(C0793R.string.id_note_for_using_external_storage);
                                }
                            }
                            C14121.this.notifyDataSetChanged();
                        }
                    });
                    return view;
                }
            };
            listView.setAdapter(r5);
            view.findViewById(C0793R.C0795id.txt_ok).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    int access$100 = SettingsListDialogFragment.this.type;
                    if (access$100 == 0) {
                        PreferenceHelper.getInstance().setPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING, strArr[SettingsListDialogFragment.this.selectedPosition]);
                    } else if (access$100 == 1) {
                        String[] stringArray = SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_frame_rate_list_values);
                        SettingsListDialogFragment.this.prefs.edit().putString("example_list_frame_rate", stringArray[SettingsListDialogFragment.this.selectedPosition]).apply();
                    } else if (access$100 == 2) {
                        String[] stringArray2 = SettingsListDialogFragment.this.getResources().getStringArray(R.array.pref_bit_rate_list_values);
                        SettingsListDialogFragment.this.prefs.edit().putString("example_list_bit_rate", stringArray2[SettingsListDialogFragment.this.selectedPosition]).apply();
                    } else if (access$100 == 3) {
                        PreferenceHelper.getInstance().setPrefRecordingOrientation(SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_orientation_list_values)[SettingsListDialogFragment.this.selectedPosition]);
                    } else if (access$100 == 4) {
                        PreferenceHelper.getInstance().setPrefRecordingCountdown(SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_count_down_list_values)[SettingsListDialogFragment.this.selectedPosition]);
                    } else if (access$100 == 6) {
                        String[] stringArray3 = SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_long_click_list_values);
                        SettingsListDialogFragment.this.prefs.edit().putString("example_list_long_click", stringArray3[SettingsListDialogFragment.this.selectedPosition]).apply();
                    } else if (access$100 == 7) {
                        PreferenceHelper.getInstance().setPrefDefaultStorageLocation(SettingsListDialogFragment.this.selectedPosition);
                    }
                    SettingsListDialogFragment.this.dismissAllowingStateLoss();
                }
            });
            view.findViewById(C0793R.C0795id.txt_cancel).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SettingsListDialogFragment.this.dismissAllowingStateLoss();
                }
            });
            view.findViewById(C0793R.C0795id.img_close_dialog).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SettingsListDialogFragment.this.dismissAllowingStateLoss();
                }
            });
        }
        String[] stringArray5 = getResources().getStringArray(C0793R.array.pref_resolution_list_titles);
        textView.setText(C0793R.string.resolution);
        textView.setCompoundDrawablesWithIntrinsicBounds(C0793R.C0794drawable.ic_resolution, 0, 0, 0);
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);
        int i3 = point.y;
        int i4 = point.x;
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray5) {
            String str2 = "x";
            String str3 = str.split(str2)[0];
            String str4 = str.split(str2)[1];
            if (i3 > i4) {
                if (Integer.parseInt(str3) <= i3 && Integer.parseInt(str4) <= i4) {
                    arrayList.add(str);
                }
            } else if (Integer.parseInt(str4) <= i3 && Integer.parseInt(str3) <= i4) {
                arrayList.add(str);
            }
        }
        strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        if (PreferenceHelper.getInstance().hasPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING)) {
            String prefResolution = PreferenceHelper.getInstance().getPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING);
            int i5 = 0;
            while (true) {
                if (i5 >= strArr2.length) {
                    break;
                } else if (strArr2[i5].startsWith(prefResolution)) {
                    this.selectedPosition = i5;
                    break;
                } else {
                    i5++;
                }
            }
        }
        z = false;
        if (!z && strArr2.length > 0) {
            PreferenceHelper.getInstance().setPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING, strArr2[0]);
        }
        strArr = strArr2;
        final String[] strArr32 = strArr;
        C14121 r52 = new ArrayAdapter<String>(getContext(), C0793R.layout.settings_dialog_list_items, strArr) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(getContext()).inflate(C0793R.layout.settings_dialog_list_items, null);
                }
                RadioButton radioButton = (RadioButton) view.findViewById(C0793R.C0795id.radio_list_item);
                radioButton.setChecked(i == SettingsListDialogFragment.this.selectedPosition);
                radioButton.setTag(Integer.valueOf(i));
                if (SettingsListDialogFragment.this.type == 7) {
                    if (StorageHelper.getInstance().externalMemoryAvailable() || i != 1) {
                        radioButton.setEnabled(true);
                    } else {
                        radioButton.setEnabled(false);
                    }
                    String str = "%s (%s / %s)";
                    if (i == 0) {
                        radioButton.setText(String.format(str, new Object[]{strArr32[i], StorageHelper.getInstance().getAvailableInternalMemorySize(), StorageHelper.getInstance().getTotalInternalMemorySize()}));
                    } else if (i == 1) {
                        if (StorageHelper.getInstance().externalMemoryAvailable()) {
                            radioButton.setText(String.format(str, new Object[]{strArr32[i], StorageHelper.getInstance().getAvailableExternalMemorySize(), StorageHelper.getInstance().getTotalExternalMemorySize()}));
                        } else {
                            radioButton.setText(String.format("%s ( %s )", new Object[]{strArr32[i], SettingsListDialogFragment.this.getString(C0793R.string.id_not_available_txt)}));
                        }
                    }
                } else {
                    radioButton.setText(strArr32[i]);
                }
                radioButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        SettingsListDialogFragment.this.selectedPosition = ((Integer) view.getTag()).intValue();
                        if (SettingsListDialogFragment.this.type == 7) {
                            int access$000 = SettingsListDialogFragment.this.selectedPosition;
                            if (access$000 == 0) {
                                SettingsListDialogFragment.this.note.setVisibility(4);
                            } else if (access$000 == 1) {
                                SettingsListDialogFragment.this.note.setVisibility(0);
                                SettingsListDialogFragment.this.note.setTextColor(-65536);
                                SettingsListDialogFragment.this.note.setText(C0793R.string.id_note_for_using_external_storage);
                            }
                        }
                        C14121.this.notifyDataSetChanged();
                    }
                });
                return view;
            }
        };
        listView.setAdapter(r52);
        view.findViewById(C0793R.C0795id.txt_ok).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int access$100 = SettingsListDialogFragment.this.type;
                if (access$100 == 0) {
                    PreferenceHelper.getInstance().setPrefResolution(Constants.TYPE_PREF_RESOLUTION_RECORDING, strArr[SettingsListDialogFragment.this.selectedPosition]);
                } else if (access$100 == 1) {
                    String[] stringArray = SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_frame_rate_list_values);
                    SettingsListDialogFragment.this.prefs.edit().putString("example_list_frame_rate", stringArray[SettingsListDialogFragment.this.selectedPosition]).apply();
                } else if (access$100 == 2) {
                    String[] stringArray2 = SettingsListDialogFragment.this.getResources().getStringArray(R.array.pref_bit_rate_list_values);
                    SettingsListDialogFragment.this.prefs.edit().putString("example_list_bit_rate", stringArray2[SettingsListDialogFragment.this.selectedPosition]).apply();
                } else if (access$100 == 3) {
                    PreferenceHelper.getInstance().setPrefRecordingOrientation(SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_orientation_list_values)[SettingsListDialogFragment.this.selectedPosition]);
                } else if (access$100 == 4) {
                    PreferenceHelper.getInstance().setPrefRecordingCountdown(SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_count_down_list_values)[SettingsListDialogFragment.this.selectedPosition]);
                } else if (access$100 == 6) {
                    String[] stringArray3 = SettingsListDialogFragment.this.getResources().getStringArray(C0793R.array.pref_long_click_list_values);
                    SettingsListDialogFragment.this.prefs.edit().putString("example_list_long_click", stringArray3[SettingsListDialogFragment.this.selectedPosition]).apply();
                } else if (access$100 == 7) {
                    PreferenceHelper.getInstance().setPrefDefaultStorageLocation(SettingsListDialogFragment.this.selectedPosition);
                }
                SettingsListDialogFragment.this.dismissAllowingStateLoss();
            }
        });
        view.findViewById(C0793R.C0795id.txt_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingsListDialogFragment.this.dismissAllowingStateLoss();
            }
        });
        view.findViewById(C0793R.C0795id.img_close_dialog).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingsListDialogFragment.this.dismissAllowingStateLoss();
            }
        });
    }
}
