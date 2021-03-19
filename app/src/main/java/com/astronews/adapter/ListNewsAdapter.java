package com.astronews.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astronews.activities.DetailArticle;
import com.astronews.activities.R;
import com.astronews.core.config.Config;
import com.astronews.core.model.News.ArticleCollection;
import com.astronews.core.model.mTranslation.Translation;
import com.astronews.core.apiServices.MTranslationService;
import com.daimajia.swipe.SwipeLayout;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.astronews.core.model.News.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}

class SurfaceViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    LinearLayout layer;
    RecyclerView.ViewHolder parentView;

    public SurfaceViewHolder(View itemView, RecyclerView.ViewHolder pv, int id) {
        layer = itemView.findViewById(id);
        parentView = pv;
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,parentView.getAdapterPosition(),false);
    }
}

class ListNewsViewHolder extends RecyclerView.ViewHolder {
    TextView article_title;
    RelativeTimeTextView article_time;
    TextView article_provider;
    SurfaceViewHolder bottom_wrapper;
    SurfaceViewHolder surface_layer;
    CircleImageView article_thumbnail;
    SwipeLayout swipe_layout;
    TextView chevron;

    public ListNewsViewHolder(View itemView) {
        super(itemView);
        article_thumbnail = (CircleImageView)itemView.findViewById(R.id.article_thumbnail);
        article_title = (TextView)itemView.findViewById(R.id.article_title);
        article_time = (RelativeTimeTextView) itemView.findViewById(R.id.article_time);
        article_provider = (TextView) itemView.findViewById(R.id.article_provider);
        swipe_layout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
        chevron = (TextView) itemView.findViewById(R.id.chevron);
        bottom_wrapper = new SurfaceViewHolder(itemView.findViewById(R.id.bottom_wrapper), this, R.id.bottom_wrapper);
        surface_layer = new SurfaceViewHolder(itemView.findViewById(R.id.surface_layer), this, R.id.surface_layer);
    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>  {
    private List<Article> article_list;
    private Context context;
    Subscription m_subscription;
    boolean translation_error_notified = false;
    SharedPreferences previously_read_articles;

    public ListNewsAdapter(ArticleCollection coll, Context context) {
        article_list = coll.getValue();
        this.context = context;
        previously_read_articles = context.getSharedPreferences(Config.PREF_FILE, 0);
    }

    @Override
    public ListNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout,parent,false);

        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListNewsViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        m_subscription = MTranslationService
                .getTranslation(article_list.get(position).getTitle())
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onCompleted() {
                        m_subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!getTranslation_error_notified())
                            Toast.makeText(context, "Cant translate title => no more credits", Toast.LENGTH_SHORT).show();
                        setTranslation_error_notified(true);
                        holder.article_title.setText(setTitle(article_list.get(holder.getAdapterPosition()).getTitle()));
                        addNewTagOnTitle(holder);
                    }

                    @Override
                    public void onNext(Translation t) {
                        holder.article_title.setText(setTitle(t.toString()));
                        addNewTagOnTitle(holder);
                    }
                });

        // Set article thumbnail
        Subscription pic_sub = Observable.from(article_list.get(position).getImage().getThumbnail())
        .subscribeOn(Schedulers.currentThread())
        .observeOn(AndroidSchedulers.handlerThread(new Handler()))
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                holder.article_thumbnail.setImageResource(R.drawable.no_img);
            }

            @Override
            public void onNext(String imgUrl) {
                Picasso.get()
                        .load(imgUrl)
                        .placeholder(R.drawable.no_img)
                        .tag(context)
                        .into(holder.article_thumbnail);
            }
        });

        // Set article Publish date
        if (article_list.get(position).getDatePublished() != null) {
                holder.article_time.setReferenceTime(parsePublicationDate(article_list.get(position)
                        .getDatePublished())
                        .getTime()
                );
        }

        //Set article Provider
        holder.article_provider.setText(article_list.get(position).getProvider().toString());

        //Set Event Click
        holder.surface_layer.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                updateArticleStatus(holder);
                //show webview of the article
                Intent detail = new Intent(context, DetailArticle.class);
                detail.putExtra("webURL",article_list.get(position).getUrl());
                detail.putExtra("title",article_list.get(position).getTitle());
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });
        // Set share event
        holder.bottom_wrapper.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Article");
                i.putExtra(Intent.EXTRA_TEXT, article_list.get(position).getUrl());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        // Set swipe Layout callbacks
        holder.swipe_layout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                holder.chevron.setBackgroundResource(R.drawable.chevron_left);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                holder.chevron.setBackgroundResource(R.drawable.chevron_right);
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        // Set animation
        Animation surfaceAnimation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        surfaceAnimation.setDuration(150);
        Animation botomAnimation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        botomAnimation.setDuration(200);
        holder.itemView.startAnimation(surfaceAnimation);
        holder.bottom_wrapper.layer.startAnimation(botomAnimation);
    }

    @Override
    public int getItemCount() {
        return article_list.size();
    }

    private Date parsePublicationDate(String input) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return df.parse(input);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String setTitle(String currentTitle) {
        // Set article Title
        if (currentTitle.length() > 75)
            currentTitle = (currentTitle.substring(0, 75) + "...");
        return currentTitle;
    }

    private void addNewTagOnTitle(ListNewsViewHolder holder) {
        String new_id = article_list.get(holder.getAdapterPosition()).getId();
        //get the previously read articles
        Set<String> new_read_set = new HashSet<String>
                (previously_read_articles.getStringSet("read_articles", new HashSet<String>()));
        if(!new_read_set.contains(new_id) && isArticleNew(holder)) {
            CharSequence currentText = holder.article_title.getText();
            holder.article_title.setText(Html.fromHtml(
                    "<font color=\"red\"><i><b>" + context.getString(R.string.new_tag) + " </b></i></font>" + currentText
            ));
        }
    }

    private void updateArticleStatus(ListNewsViewHolder holder) {
        String new_id = article_list.get(holder.getAdapterPosition()).getId();
        //get the previously read articles
        Set<String> new_read_set = new HashSet<String>
                (previously_read_articles.getStringSet("read_articles", new HashSet<String>()));

        if(!new_read_set.contains(new_id) && isArticleNew(holder)) {
            // create the editor and add the newly clicked article to the set
            new_read_set.add(new_id);
            SharedPreferences.Editor editor = previously_read_articles.edit();
            editor.putStringSet("read_articles", new_read_set);
            //Also add the the date of said article ["id"] -> publishedDate for it to be cleansed later
            editor.putString(new_id, article_list.get(holder.getAdapterPosition()).getDatePublished().toString());
            // Apply the edits!
            editor.apply();
            holder.article_title.setText(article_list.get(holder.getAdapterPosition()).getTitle());
        }
    }

    private boolean isArticleNew(ListNewsViewHolder holder) {
        //test if the article is newer than last 2 days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        Date old_thershold = calendar.getTime();
        Date article_published_date = parsePublicationDate(article_list.get(holder.getAdapterPosition()).getDatePublished());
        return article_published_date.after(old_thershold);
    }

    public boolean getTranslation_error_notified() {return translation_error_notified;}
    public void  setTranslation_error_notified(boolean nv) {translation_error_notified = nv;}
}
