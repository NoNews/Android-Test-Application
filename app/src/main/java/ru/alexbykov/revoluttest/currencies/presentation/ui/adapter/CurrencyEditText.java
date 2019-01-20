package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@SuppressLint("AppCompatCustomView")
public class CurrencyEditText extends EditText {
    private static String prefix = "";
    private static final int MAX_LENGTH = 16;
    private static final int MAX_DECIMAL_DIGIT = 3;

    int maxLength = MAX_LENGTH;
    private CurrencyTextWatcher currencyTextWatcher = new CurrencyTextWatcher(this, prefix, maxLength);

    public CurrencyEditText(Context context) {
        this(context, null);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setHint(prefix);
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        removeTextChangedListener(currencyTextWatcher);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        currencyTextWatcher = null;
        currencyTextWatcher = new CurrencyTextWatcher(this, prefix, maxLength);
        addTextChangedListener(currencyTextWatcher);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            this.addTextChangedListener(currencyTextWatcher);
        } else {
            this.removeTextChangedListener(currencyTextWatcher);
        }
        handleCaseCurrencyEmpty(focused);
    }

    /**
     * When currency empty <br/>
     * + When focus EditText, set the default text = prefix (ex: VND) <br/>
     * + When EditText lose focus, set the default text = "", EditText will display hint (ex:VND)
     */
    private void handleCaseCurrencyEmpty(boolean focused) {
        if (focused) {
            if (getText().toString().isEmpty()) {
                setText(prefix);
            }
        } else {
            if (getText().toString().equals(prefix)) {
                setText("");
            }
        }
    }

    private static class CurrencyTextWatcher implements TextWatcher {
        private final EditText editText;
        private String previousNumber;
        private String prefix;
        private int maxLength;
        DecimalFormat integerFormatter;

        /**
         * I always use locale US instead of default to make DecimalFormat work well in all language
         */
        CurrencyTextWatcher(EditText editText, String prefix, int maxLength) {
            this.maxLength = maxLength;
            this.editText = editText;
            this.prefix = prefix;
            integerFormatter = new DecimalFormat("#,###.###", new DecimalFormatSymbols(Locale.US));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (!editText.hasFocus()){
                return;
            }

            String str = editable.toString();
            if (str.length() < prefix.length()) {
                editText.setText(prefix);
                editText.setSelection(prefix.length());
                return;
            }
            if (str.equals(prefix)) {
                return;
            }
            // number this the string which not contain prefix and ,
            String number = str.replace(prefix, "").replaceAll("[,]", "");
            // for prevent afterTextChanged recursive call
            if (number.equals(previousNumber) || number.isEmpty()) {
                return;
            }
            previousNumber = number;

            String formattedString = prefix + formatNumber(number);
            editText.removeTextChangedListener(this); // Remove listener
            editText.setText(formattedString);
            handleSelection();
            editText.addTextChangedListener(this); // Add back the listener
        }

        private String formatNumber(String number) {
            if (number.contains(".")) {
                return formatDecimal(number);
            }
            return formatInteger(number);
        }

        private String formatInteger(String str) {
            BigDecimal parsed = new BigDecimal(str);
            return integerFormatter.format(parsed);
        }

        private String formatDecimal(String str) {
            if (str.equals(".")) {
                return ".";
            }
            BigDecimal parsed = new BigDecimal(str);
            // example pattern VND #,###.00
            DecimalFormat formatter = new DecimalFormat("#,###." + getDecimalPattern(str),
                    new DecimalFormatSymbols(Locale.US));
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(parsed);
        }

        /**
         * It will return suitable pattern for format decimal
         * For example: 10.2 -> return 0 | 10.23 -> return 00, | 10.235 -> return 000
         */
        private String getDecimalPattern(String str) {
            int decimalCount = str.length() - str.indexOf(".") - 1;
            StringBuilder decimalPattern = new StringBuilder();
            for (int i = 0; i < decimalCount && i < MAX_DECIMAL_DIGIT; i++) {
                decimalPattern.append("0");
            }
            return decimalPattern.toString();
        }

        private void handleSelection() {
            if (editText.getText().length() <= maxLength) {
                editText.setSelection(editText.getText().length());
            } else {
                editText.setSelection(maxLength);
            }
        }
    }


}
