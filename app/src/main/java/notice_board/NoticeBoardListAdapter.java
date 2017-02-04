package notice_board;

import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.example.adam.quickroster.R;

import java.util.List;

public class NoticeBoardListAdapter extends BaseAdapter {

    private List<String> notices;
    private Context context;
    private boolean editable;

    public NoticeBoardListAdapter(Context context, List<String> notices, boolean editable) {
        this.notices = notices;
        this.context = context;
        this.editable = editable;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_notice_board_list_adapter, parent, false);

        final String notice = notices.get(position);
        final EditText textBox = (EditText) view.findViewById(R.id.noticeMessage);
        textBox.setEnabled(editable);
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notices.set(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textBox.setText(notice);
        return view;
    }

    public List<String> getNotices() {
        return this.notices;
    }
}
