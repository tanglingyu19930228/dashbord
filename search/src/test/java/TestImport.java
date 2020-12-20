import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.search.SearchApplication;
import com.search.common.utils.DateUtils;
import com.search.common.utils.ExcelUtils;
import com.search.common.utils.StringUtils;
import com.search.dao.SysArticleDao;
import com.search.entity.SysArticleEntity;
import io.swagger.models.auth.In;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = { SearchApplication.class })
@RunWith(SpringRunner.class)
public class TestImport {


    @Autowired
    SysArticleDao sysArticleDao;

    @Test
    public void importData() throws Exception{

        final List<List<String>> lists = ExcelUtils.importXls("D:/singe.xls", 0);

        List<SysArticleEntity > list = new ArrayList<>();

        for (int i = 1; i < lists.size(); i++) {
            final SysArticleEntity sysArticleEntity = transferOneExcel(lists.get(i));
            list.add(sysArticleEntity);
        }

        List<SysArticleEntity>  insert = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final List<SysArticleEntity> objects = new ArrayList<>();
            objects.add(list.get(i));
            try {
                final int i1 = this.sysArticleDao.insertSysArticle(objects);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        System.out.println("");
    }

    private static String getIntegerString(String one){
        if(StringUtils.isNotBlank(one)){
            if(one.contains(".")){
                return one.substring(0,one.indexOf("."));
            }
            return one;
        }else{
            return null;
        }
    }

    private static SysArticleEntity transferOneExcel(List<String> listOne){

        SysArticleEntity sysArticleEntity = new SysArticleEntity();
        sysArticleEntity.setDataId(StringUtils.isBlank(listOne.get(2))?null:Integer.parseInt(getIntegerString(listOne.get(2))));
        sysArticleEntity.setTitleId(listOne.get(3));
        sysArticleEntity.setTopicId(listOne.get(4));
        sysArticleEntity.setContentId(StringUtils.isNotBlank(listOne.get(5))?Integer.parseInt(getIntegerString(listOne.get(5))):0);
        sysArticleEntity.setContent(listOne.get(6));
        final String s = listOne.get(7);
        sysArticleEntity.setEmotionType(StringUtils.isBlank(s)?0:s.equals("正面")?1:2);
        sysArticleEntity.setEmotionValue(StringUtils.isBlank(listOne.get(8))?0:Integer.parseInt(getIntegerString(listOne.get(8))));
        sysArticleEntity.setInsertTime(DateUtils.getNowDate());
        final String s1 = listOne.get(14);
        sysArticleEntity.setMediaType(StringUtils.isBlank(s1)?-1:s1.equals("微信")?0:s1.equals("微博")?1:s1.equals("博客")?2:s1.equals("论坛")?3:s1.equals("问答")?4:5);
        sysArticleEntity.setPublisher(listOne.get(15));
        sysArticleEntity.setPublisherId(StringUtils.isBlank(listOne.get(16))?null:getIntegerString(listOne.get(16)));
        sysArticleEntity.setPublisherLinkNum(StringUtils.isBlank(listOne.get(21))?null: Integer.parseInt(getIntegerString(listOne.get(21))));
        sysArticleEntity.setPublisherPv(StringUtils.isBlank(listOne.get(22))?null: Integer.parseInt(getIntegerString(listOne.get(22))));
        sysArticleEntity.setPublisherCommentNum(StringUtils.isBlank(listOne.get(23))?null: Integer.parseInt(getIntegerString(listOne.get(23))));
        sysArticleEntity.setPublisherCollectionNum(StringUtils.isBlank(listOne.get(24))?null: Integer.parseInt(getIntegerString(listOne.get(24))));
        sysArticleEntity.setPublisherRepostNum(StringUtils.isBlank(listOne.get(25))?null: Integer.parseInt(getIntegerString(listOne.get(25))));
        sysArticleEntity.setPublisherSiteName(StringUtils.isBlank(listOne.get(26))?"": listOne.get(26));
        sysArticleEntity.setPublisherArticleTitle(StringUtils.isBlank(listOne.get(27))?"": listOne.get(27));
        sysArticleEntity.setPublisherArticleUnique(StringUtils.isBlank(listOne.get(29))?"": listOne.get(29));
        sysArticleEntity.setPublisherArticleUrl(StringUtils.isBlank(listOne.get(30))?"": listOne.get(30));
        sysArticleEntity.setPublisher("ce0b0d401837e2712331483dc0b611c6");
        sysArticleEntity.setPublisherUserType(StringUtils.isBlank(listOne.get(18))?0:listOne.get(18).equals("个人认证")?0:listOne.get(18).equals("普通用户")?1:2);
        //20
        sysArticleEntity.setPublisherTime(DateUtils.getNowDate());
        return sysArticleEntity;
    }

}
