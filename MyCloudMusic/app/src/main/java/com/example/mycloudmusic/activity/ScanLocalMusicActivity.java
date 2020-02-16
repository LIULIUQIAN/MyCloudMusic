package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.SongLocal;
import com.example.mycloudmusic.domain.event.ScanMusicCompleteEvent;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.StorageUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanLocalMusicActivity extends BaseTitleActivity {

    @BindView(R.id.tv_progress)
    TextView tv_progress;

    @BindView(R.id.iv_scan_music_background)
    ImageView iv_scan_music_background;

    @BindView(R.id.iv_scan_music_line)
    ImageView iv_scan_music_line;

    @BindView(R.id.iv_scan_music_zoom)
    ImageView iv_scan_music_zoom;

    @BindView(R.id.bt_scan)
    Button bt_scan;
    private AsyncTask<Void, String, List<SongLocal>> scanMusicAsyncTask;

    /**
     * 是否扫描完成
     */
    private boolean isScanComplete;

    /**
     * 是否找到了音乐
     */
    private boolean hasFoundMusic;

    /**
     * 是否在扫描中
     */
    private boolean isScanning;

    /**
     * 扫描线动画
     */
    private TranslateAnimation lineAnimation;

    /**
     * 放大镜动画
     */
    private ValueAnimator zoomValueAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local_music);
    }


    /**
     * 扫描按钮点击了
     */
    @SuppressLint("ResourceType")
    @OnClick(R.id.bt_scan)
    public void onScanClick() {
        //如果扫描完成了
        //点击按钮就是关闭界面
        //扫描按钮的样式在扫描完成的方法里面更改了
        if (isScanComplete) {
            finish();
            return;
        }

        if (isScanning) {
            //扫描中

            //停止扫描
            stopScan();

            //设置按钮背景
            bt_scan.setBackgroundResource(R.drawable.selector_color_primary);

            //设置按钮文本颜色
            bt_scan.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary_reverse));

            //设置按钮文本
            bt_scan.setText(R.string.start_scan);
        } else {
            //没有扫描

            //开始扫描
            startScan();

            //按钮背景
            bt_scan.setBackgroundResource(R.drawable.selector_color_primary_reverse);

            //按钮文本颜色
            bt_scan.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary));

            //文本
            bt_scan.setText(R.string.stop_scan);
        }

        //改变扫描状态
        isScanning = !isScanning;

    }

    /**
     * 开始扫描
     */
    private void startScan() {
        //扫描线
        //不使用属性动画
        //是因为属性动画要获取坐标

        //创建一个位移动画
        //RELATIVE_TO_PARENT：表示位置是相对父类的
        //0：就表示现在的位置
        //1：就表示在父类的另一边
        //例如：y开始位置为0
        //结束位置为1
        //就表示从父容器顶部移动到父容器底部
        lineAnimation = new TranslateAnimation(
                //原x坐标类型；坐标值
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,

                //目标x坐标类型；坐标值
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,

                //原y坐标类型；坐标值
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,

                //目标y坐标类型；坐标值
                TranslateAnimation.RELATIVE_TO_PARENT, 0.7f
        );

        //设置插值器
        //这里设置的是减速插值器
        //也就是说开始速度很快慢慢变慢
        //可以理解为人刚开始有劲干活
        //后面就没劲了
        //所以速度就慢了
        lineAnimation.setInterpolator(new DecelerateInterpolator());

        //设置动画时间
        lineAnimation.setDuration(2000);

        //重复次数
        //-1:无限重复
        lineAnimation.setRepeatCount(-1);

        //重复模式
        //REVERSE：表示从开始位置到结束位置
        //然后从结束位置到开始位置
        lineAnimation.setRepeatMode(TranslateAnimation.REVERSE);

        //设置动画监听器
        lineAnimation.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动画开始
             *
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {
                //显示线
                iv_scan_music_line.setVisibility(View.VISIBLE);
            }

            /**
             * 动画结束
             *
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                //隐藏线
                iv_scan_music_line.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //清除原来的动画
        iv_scan_music_line.clearAnimation();

        //执行动画
        iv_scan_music_line.startAnimation(lineAnimation);

        //放大镜搜索效果

        //其实下面动画的效果就是
        //让放大镜中心在一个圆周上做运动
        zoomValueAnimator = ValueAnimator.ofFloat(0.0f, 360.0F);

        //设置插值器
        zoomValueAnimator.setInterpolator(new LinearInterpolator());

        //动画时间
        zoomValueAnimator.setDuration(30000);

        //无限重复
        zoomValueAnimator.setRepeatCount(-1);

        //重复模式
        //重新开始
        zoomValueAnimator.setRepeatMode(ValueAnimator.RESTART);

        //动画监听器
        zoomValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /**
             * 每次动画调用
             *
             * @param animation
             */
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取角度
                float angle = (float) animation.getAnimatedValue();

                //由于系统需要的是x,y
                //所以通过三角函数计算出x,y

                //余弦值
                float translateX = (float) (Constant.DEFAULT_RADIUS * Math.cos(angle));

                //正弦值
                float translateY = (float) (Constant.DEFAULT_RADIUS * Math.sin(angle));

                //设置偏移位置

                //现在是顺时针
                //如果颠倒x,y
                //就变成逆时针了
                //如果y先是正数就是顺时针
                iv_scan_music_zoom.setTranslationX(translateX);
                iv_scan_music_zoom.setTranslationY(translateY);
            }
        });

        //开始动画
        zoomValueAnimator.start();

        //扫描音乐
        startScanMusic();
    }

    /**
     * 扫描音乐
     * 我们这里只扫描媒体库
     * 不全盘扫描
     * 扫描媒体库的好处是：
     * 不用手动解析音乐
     * 但也有弊端：
     * 媒体库更新可能不及时
     * 大部分手机都重启后才会刷新媒体库
     * 还有就是媒体库也不能识别所以格式
     * 所以如果要做一个商业级的音乐软件
     * 那么可能还需要全盘扫描
     * 手动解析音乐
     * 但这个我们就超出我们课程范围了
     * 因为我们这个课程定位是一个通用课程
     * 所以大家学到这里就够了
     * 如果以后需要做商业级音乐软件
     * 在去深入学习这方面知识
     */
    private void startScanMusic() {
        //扫描音乐是耗时任务
        //所以放到子线程
        //这里用的是AsyncTask
        scanMusicAsyncTask = new AsyncTask<Void, String, List<SongLocal>>() {

            /**
             * 子线程中执行
             *
             * @param voids
             * @return
             */
            @Override
            protected List<SongLocal> doInBackground(Void... voids) {
                //创建结果列表
                List<SongLocal> datum = new ArrayList<>();

                /**
                 * 使用内容提供者查询
                 * 我们这里是查询音乐大小大于1M，时间大于60秒
                 * @param uri 资源标识符
                 * @param projection 选择那些字段
                 * @param selection 条件
                 * @param selectionArgs 条件参数
                 * @param sortOrder 排序
                 */
                Cursor cursor = getContentResolver().query(
                        //查询的地址
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,

                        //要返回数据的字段
                        new String[]{
                                //媒体Id
                                BaseColumns._ID,

                                //媒体标题
                                MediaStore.Audio.AudioColumns.TITLE,

                                //艺术家（歌手）
                                MediaStore.Audio.AudioColumns.ARTIST,

                                //专辑
                                MediaStore.Audio.AudioColumns.ALBUM,

                                //专辑id
                                MediaStore.Audio.AudioColumns.ALBUM_ID,

                                //文件路径
                                MediaStore.Audio.AudioColumns.DATA,

                                //显示的名称
                                //例如:"爱的代价.mp3"
                                MediaStore.Audio.AudioColumns.DISPLAY_NAME,

                                //文件大小
                                //单位：字节
                                MediaStore.Audio.AudioColumns.SIZE,

                                //时长
                                //单位：毫秒
                                MediaStore.Audio.AudioColumns.DURATION
                        },

                        //查询条件
                        Constant.MEDIA_AUDIO_SELECTION,

                        //查询参数
                        new String[]{
                                String.valueOf(Constant.MUSIC_FILTER_SIZE),
                                String.valueOf(Constant.MUSIC_FILTER_DURATION),
                        },

                        //排序方式
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
                );

                //遍历数据
                while (cursor != null && cursor.moveToNext()) {
                    //有数据

                    //遍历每一行数据

                    //像封面这些信息
                    //从文件中获取的很有可能不对
                    //所以真实项目中
                    //一般是根据标题，歌手，专辑等信息
                    //去服务端匹配封面，歌词等信息
                    //这里我们就不在实现该功能了

                    //媒体id
                    long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));

                    //媒体标题
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));

                    //艺术家（歌手）
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));

                    //专辑
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));

                    //专辑id
                    String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));

                    //文件路径
                    //DATA列在Android Q以下版本代表了文件路径
                    //但在 Android Q上该路径无法被访问

                    //获取这个路径的目的是
                    //扫描的时候显示到界面上
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));

                    //需要通过这种方式方法
                    //该方法也兼容Android Q以下版本
                    String uri = StorageUtil.getAudioContentUri(id);

                    //显示名称
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                    //文件大小
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                    //时长
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Log.d("tag", String.format("doInBackground:%d,%s,%s,%s", id, title, artist, album));

                    //保存音乐
                    SongLocal data = new SongLocal();

                    //设置id
                    data.setId(String.valueOf(id));

                    //标题
                    data.setTitle(title);

                    //歌手
                    data.setSinger_nickname(artist);

                    //由于我们项目没有实现专辑
                    //所以这些字段也就不保存了

                    //时长
                    //也可以不保存
                    //因为我们是在播放的时候获取
                    data.setDuration(duration);

                    //路径
                    data.setUri(uri);

                    //来源
                    data.setSource(SongLocal.SOURCE_LOCAL);

                    //添加到列表
                    datum.add(data);

                    //保存到数据库
                    orm.saveSongLocal(data);

                    //发布通知
                    publishProgress(path);

                    //这里模拟延迟
                    SystemClock.sleep(1000);
                }

                //返回结果
                return datum;
            }

            /**
             * @param songs
             */
            @SuppressLint("ResourceType")
            @Override
            protected void onPostExecute(List<SongLocal> songs) {
                super.onPostExecute(songs);

                //清除异步任务
                scanMusicAsyncTask = null;

                //扫描完成
                isScanComplete = true;

                //是否找到了音乐
                hasFoundMusic = songs.size() > 0;

                //停止扫描
                stopScan();

                //显示扫描到的音乐数量
                tv_progress.setText(getResources().getString(R.string.found_music_count, songs.size()));

                //设置按钮背景
                bt_scan.setBackgroundResource(R.drawable.selector_color_primary);

                //设置按钮文本颜色
                bt_scan.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary_reverse));

                //设置按钮文本
                bt_scan.setText(R.string.to_my_music);
            }

            /**
             * 进度回调方法
             * 主线程中执行
             * 调用publishProgress就会执行该方法
             *
             * @param values
             */
            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                //获取传递的值
                String value = values[0];
                //设置进度到文本
                tv_progress.setText(value);
            }
        };

        //启动异步任务
        scanMusicAsyncTask.execute();
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        //取消异步任务
        if (scanMusicAsyncTask != null) {
            scanMusicAsyncTask.cancel(true);
            scanMusicAsyncTask = null;
        }

        //清除扫描先动画
        iv_scan_music_line.clearAnimation();

        //隐藏扫描扫描线
        iv_scan_music_line.setVisibility(View.GONE);

        //取消扫描线动画
        if (lineAnimation != null) {
            lineAnimation.cancel();
            lineAnimation = null;
        }

        //清除放大镜动画
        iv_scan_music_zoom.clearAnimation();

        //取消放大镜动画
        if (null != zoomValueAnimator) {
            zoomValueAnimator.cancel();
            zoomValueAnimator=null;
        }
    }

    /**
     * 界面销毁时
     */
    @Override
    protected void onDestroy() {
        if (hasFoundMusic) {
            //找到了音乐发送通知
            EventBus.getDefault().post(new ScanMusicCompleteEvent());
        }

        //停止扫描
        stopScan();

        super.onDestroy();
    }

}
