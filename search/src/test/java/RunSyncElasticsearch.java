import com.search.SearchApplication;
import com.search.common.utils.R;
import com.search.sync.CombineDatabaseAndElasticsearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = { SearchApplication.class })
@RunWith(SpringRunner.class)
public class RunSyncElasticsearch {

    @Autowired
    CombineDatabaseAndElasticsearch combineDatabaseAndElasticsearch;

    @Test
    public void syncElasticsearch(){
        final R r = combineDatabaseAndElasticsearch.doSync();
    }

}
