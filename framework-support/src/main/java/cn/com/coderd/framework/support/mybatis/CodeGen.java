package cn.com.coderd.framework.support.mybatis;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * mybatis 代码生成器封装
 */
@Data
@Accessors(chain = true)
public class CodeGen implements Serializable {
    private String driverName;
    private String url;
    private String username;
    private String password;
    private String packageName;
    private String tableNameLike;

    /**
     * 代码生成
     *
     * @throws Exception
     */
    public void execute() throws Exception {
        Path path = Paths.get(System.getProperty("user.dir"), "/target/generated-sources/mybatis");
        FileUtils.deleteDirectory(path.toFile());

        Class.forName(this.driverName);
        DataSourceConfig dataSourceConfig = new DataSourceConfig
                .Builder(this.url, this.username, this.password).build();
        AutoGenerator mpg = new AutoGenerator(dataSourceConfig);

        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .author("coderd")
                .outputDir(path.toFile().getAbsolutePath())
                .openDir(false)
                .dateType(DateType.TIME_PACK).build();
        mpg.global(globalConfig);

        PackageConfig pc = new PackageConfig.Builder(this.packageName, "domain").build();
        mpg.packageInfo(pc);

        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .likeTable(new LikeTable(this.tableNameLike == null ? "" : this.tableNameLike))
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .enableChainModel()
                .enableTableFieldAnnotation()
                .enableSerialVersionUID()
                .mapperBuilder()
                .enableBaseColumnList()
                .enableBaseResultMap()
                .serviceBuilder()
                .build();

        mpg.strategy(strategyConfig);
        TemplateConfig templateConfig = new TemplateConfig.Builder()
                .disable(TemplateType.CONTROLLER).build();

        mpg.template(templateConfig);

        mpg.execute();
        System.out.println("代码已经生成:[" + path.toFile().getAbsolutePath() + "]");
    }
}
