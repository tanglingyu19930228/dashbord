import com.search.SearchApplication;
import com.search.common.utils.ExcelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest(classes = { SearchApplication.class })
@RunWith(SpringRunner.class)
public class TestCreateExcel {

    @Test
    public void testExportFile(){

        try {
//             ExcelUtils.exportExcel("list", null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
