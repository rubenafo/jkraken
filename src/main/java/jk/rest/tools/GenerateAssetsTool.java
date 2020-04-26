package jk.rest.tools;

import jk.rest.api.KrakenRestService;
import jk.rest.entities.AssetPairs;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.SneakyThrows;
import lombok.var;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

public class GenerateAssetsTool {

    @SneakyThrows
    private static void createEnum(AssetPairs assetPairs) {
                var enumTypeBuilder = TypeSpec.enumBuilder("AssetPairs")
                        .addModifiers(Modifier.PUBLIC)
                        .addField(String.class, "altName")
                        .addField(String.class, "wsName")
                        .addField(String.class, "aClassBase")
                        .addField(String.class, "aClassQuote")
                        .addField(String.class, "base")
                        .addField(String.class, "quote")
                        .addField(String.class, "lot")
                        .addField(String.class, "feeVolCurrency")
                        .addField(TypeName.INT, "pairDecimal")
                        .addField(TypeName.INT, "lotDecimal")
                        .addField(TypeName.INT, "lotMultiplier")
                        .addField(TypeName.INT, "marginCall")
                        .addField(TypeName.INT, "marginStop")
                        .addMethod(MethodSpec.constructorBuilder()
                                .addParameter(String.class, "altName").addStatement("this.$N = $N", "altName", "altName")
                                .addParameter(String.class, "wsName").addStatement("this.$N = $N", "wsName", "wsName")
                                .addParameter(String.class,        "aClassBase").addStatement("this.$N = $N", "aClassBase", "aClassBase")
                                .addParameter(String.class,        "aClassQuote").addStatement("this.$N = $N", "aClassQuote", "aClassQuote")
                                .addParameter(String.class,        "quote").addStatement("this.$N = $N", "quote", "quote")
                                .addParameter(String.class,        "lot").addStatement("this.$N = $N", "lot", "lot")
                                .addParameter(String.class,        "base").addStatement("this.$N = $N", "base", "base")
                                .addParameter(String.class,        "feeVolCurrency").addStatement("this.$N = $N", "feeVolCurrency", "feeVolCurrency")
                                .addParameter(TypeName.INT, "pairDecimal").addStatement("this.$N = $N", "pairDecimal", "pairDecimal")
                                .addParameter(TypeName.INT, "lotDecimal").addStatement("this.$N = $N", "lotDecimal", "lotDecimal")
                                .addParameter(TypeName.INT, "lotMultiplier").addStatement("this.$N = $N", "lotMultiplier", "lotMultiplier")
                                .addParameter(TypeName.INT, "marginCall").addStatement("this.$N = $N", "marginCall", "marginCall")
                                .addParameter(TypeName.INT, "marginStop").addStatement("this.$N = $N", "marginStop", "marginStop")
                        .build());

                // create constants
                assetPairs.getResult().entrySet().forEach(entry -> {
                    var pairName = entry.getKey();
                    pairName = pairName.replace(".", "_");
                    var attrs = entry.getValue();
                    enumTypeBuilder.addEnumConstant(pairName,
                            TypeSpec.anonymousClassBuilder("$S, $S, $S, $S, $S, $S, $S, $S, $L, $L, $L, $L, $L",
                                    attrs.getAltname(), attrs.getWsname(), attrs.getAclass_base(), attrs.getBase(), attrs.getAclass_quote(), attrs.getQuote(), attrs.getLot(),
                                    attrs.getFee_volume_currency(), attrs.getPair_decimals(), attrs.getLot_decimals(), attrs.getLot_multiplier(), attrs.getMargin_call(), attrs.getMargin_stop())
                                    .build());
                });

        TypeSpec assetEnums = TypeSpec.classBuilder("AssetPairsEnum")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(enumTypeBuilder.build())
               .build();
        JavaFile javaFile = JavaFile.builder("jk.rest.entities", assetEnums).build();
        System.out.println("Creating AssetPairsEnum.java file under co.jkraken.entities...");
        javaFile.writeToPath(Paths.get("src/main/java/"));

    };

    @SneakyThrows
    public static void main (String ...args) {
        var assetPairs = KrakenRestService.getAssetPairs();
        GenerateAssetsTool.createEnum(assetPairs);
    }
}
