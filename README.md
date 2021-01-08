# PagingListView
一款好用的分页ListView,可以结合后台请求分页数据使用

[![](https://jitpack.io/v/lijiangyong0628/PagingListView.svg)](https://jitpack.io/#lijiangyong0628/PagingListView)

依赖方法：
1.
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

2.当前1.0.0

	dependencies {
	        implementation 'com.github.lijiangyong0628:PagingListView:1.0.0'
	}

以下为demo使用方法，可以参考，有注释

public class MainActivity extends AppCompatActivity {

    private PagingListView demoListView;
    private InforAdapter inforAdapter;//必须实现，具体界面可以自定义
    private PagingListViewPresent<String> present;
    private LinearLayout inforLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
    }

    private void initView() {
        demoListView = findViewById(R.id.demoListView);
        inforLinearLayout = findViewById(R.id.inforLinearLayout);
        inforAdapter = new InforAdapter<String>(this) {

            @Override
            public int getLayoutId() {
                return R.layout.adpater_infor_list;
            }

            @Override
            public void setAdapterView(View itemView, String data, int position) {
                TextView tv = itemView.findViewById(R.id.contentTv);
                tv.setText(data);
            }
        };
        present = new PagingListViewPresent<String>(demoListView, 30, this) {
            @Override
            public void addAdapterList(List<String> temp) {
                //此处添加加载成功后的分页数据进listview，此处通过adpater add进去
                inforAdapter.addList(temp);
            }

            @Override
            public void getHttpData(boolean first, int curentPageNo, int pageSize, Handler handler) {
                //此处根据自己的业务需求，一般是编写http请求，然后将请求获取的数据填充进来，我这边是请求后台数据，把当前页，页面数据大小传给后台请求获得分页数据

                //此处用demo数据填充
                //必须要返回这个对象数据
                HttpStatus status = new HttpStatus();

                //将数据list设置进去
                status.obj = getDemoList(curentPageNo, pageSize);

                //设置成功获取数据   根据业务需求，如果请求失败，未返回数据则为false
                status.success = true;

                //设置是否有下一页，为1则有下一页，为0则数据加载分页数据完成，一般请求后后台会返回是否有下一页标志，为了测试默认写死1
                status.hasNext = 1;

                //以下默认结构，复制即可
                Message message = new Message();
                if (first) {
                    message.what = PagingListViewPresent.UPDATE_FIRST;//发送handler通知更新第一页数据
                } else {
                    message.what = PagingListViewPresent.UPDATE_NEXT;//发送通知更新第二页及其他页数据
                }
                message.obj = status;//将成功的数据设置进消息体
                handler.sendMessage(message);//以上数据体必须有，此处是将Message发送进去处理
            }

            @Override
            public void setAdapterList(List<String> list) {
                //此处很清楚，就是给分页listview setAdapter
                inforLinearLayout.setVisibility(View.GONE);
                demoListView.setVisibility(View.VISIBLE);
                inforAdapter.setList(list);
                demoListView.setAdapter(inforAdapter);
            }

            @Override
            public void hideListView() {
                //此处如果没有加载到数据，可以将listview隐藏，然后显示自己需要的界面
                demoListView.setVisibility(View.GONE);
                inforLinearLayout.setVisibility(View.VISIBLE);
            }
        };

        present.init();//开始加载数据
    }

    private List<String> getDemoList(int pageNo, int pageSize) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            list.add("pageNo:" + pageNo + ",pageSize：" + pageSize);
        }
        return list;
    }
}
