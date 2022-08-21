package jp.com.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    static final String URL = "http://enjoygarden.net/sample/tcs/webview_test.html";
    private WebView webView;
    private Button set;
    private Button get;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set = findViewById(R.id.set);
        set.setEnabled(false);
        get = findViewById(R.id.get);
        get.setEnabled(false);

        // WebViewの初期設定
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            // http://s-prism3.seesaa.net/article/435721034.html
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                /* 読み込み開始 */
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                /* 読み込み終了 */
                set.setEnabled(true);
                get.setEnabled(true);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                /* 読み込み失敗 */
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);   // JavaScriptを有効にする
        webView.loadUrl(URL);                               // URLを読み込む

        // PUTボタン押下
        set.setOnClickListener( v -> {
            // テキストエリアの値を取得
            EditText et = findViewById(R.id.textarea);
            SpannableStringBuilder sb = (SpannableStringBuilder)et.getText();
            String str = sb.toString();
            // webViewに設定
            String script ="document.getElementById('value').value = '" + str + "';";
            webView.evaluateJavascript(script, null);
        });

        // GETボタン押下
        get.setOnClickListener( v -> {
            // ①ActivityからJavaScriptを流し込む方法
            // ②webView内のJavaScriptを起動させる方法
            // の２通りあるっぽい。今回は①を実装

            // webViewから取得
            String script ="document.getElementById('value').value;";
            webView.evaluateJavascript(script, s -> {
                // テキストエリアに設定
                EditText et = findViewById(R.id.textarea);
                et.setText(s);
            });
        });

    }

}