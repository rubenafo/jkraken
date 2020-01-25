package com.jkraken.tools;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.SneakyThrows;
import lombok.var;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

public class GenerateAssetsTool {

    @SneakyThrows
    public static void main (String ...args) {

                var enumType = TypeSpec.enumBuilder("AssetPairs")
                        .addModifiers(Modifier.PUBLIC)
                        .addField(String.class, "altName")
                        .addField(String.class, "wsName")
                        .addField(String.class, "aClassBase")
                        .addField(String.class, "aClassQuote")
                        .addField(String.class, "base")
                        .addField(String.class, "quote")
                        .addField(String.class, "lot")
                        .addField(TypeName.INT, "pairDecimal")
                        .addField(TypeName.INT, "lotDecimal")
                        .addField(TypeName.INT, "lotMultiplier")
                        .addField(TypeName.INT, "marginCall")
                        .addField(TypeName.INT, "marginStop")
                        .addField(TypeName.INT, "leverageBuy")
                        .addMethod(MethodSpec.constructorBuilder()
                                .addParameter(String.class, "altName").addStatement("this.$N = $N", "altName", "altName")
                                .addParameter(String.class, "wsName").addStatement("this.$N = $N", "wsName", "wsName")
                                .addParameter(String.class,        "aclassBase").addStatement("this.$N = $N", "aClassBase", "aClassBase")
                                .addParameter(String.class,        "aclassQuote").addStatement("this.$N = $N", "aClassQuote", "aClassQuote")
                                .addParameter(String.class,        "quote").addStatement("this.$N = $N", "quote", "quote")
                                .addParameter(String.class,        "lot").addStatement("this.$N = $N", "lot", "lot")
                                .addParameter(String.class,        "base").addStatement("this.$N = $N", "base", "base")
                                .addParameter(String.class,        "feeVolCurrency").addStatement("this.$N = $N", "feeVolCurrency", "feeVolCurrency")
                                .addParameter(TypeName.INT, "pairDecimal").addStatement("this.$N = $N", "pairDecimal", "pairDecimal")
                                .addParameter(TypeName.INT, "lotDecimal").addStatement("this.$N = $N", "lotDecimal", "lotDecimal")
                                .addParameter(TypeName.INT, "lotMultiplier").addStatement("this.$N = $N", "lotMultiplier", "lotMultiplier")
                                .addParameter(TypeName.INT, "marginCall").addStatement("this.$N = $N", "marginCall", "marginCall")
                                .addParameter(TypeName.INT, "marginStop").addStatement("this.$N = $N", "marginStop", "marginStop")
                                .addParameter(TypeName.OBJECT, "leverageBuy").addStatement("this.$N = $N", "leverageBuy", "leverageBuy")
                        .build())
                        .addModifiers()
                        .addEnumConstant("ADAETH",
                                TypeSpec.anonymousClassBuilder("$S, $S, $S, $S, $S, $S, $S, $S $L, $L, $L, $L, $L","ADAETH", "ADA/ETH", "currency", "ADA", "currency", "XETH", "unit", "ZUSD", 1,1,1,1,1)
                                        .build())
                .build();

        TypeSpec assetEnums = TypeSpec.classBuilder("AssetEnums")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(enumType)
               .build();

        JavaFile javaFile = JavaFile.builder("com.jkraken.entities", assetEnums)
                .build();

        javaFile.writeTo(System.out);
    };
}
