package com.jkraken.entities;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
public class AssetPair {

    private String altname;
    private String wsname;
    private String aclass_base;
    private String base;
    private String aclass_quote;
    private String quote;
    private String lot;
    private int pair_decimals;
    private int lot_decimals;
    private int lot_multiplier;
    private List<Integer> leverage_buy;
    private List<Integer> leverage_sell;
    private List<List<Double>> fees;
    private List<List<Double>> fees_maker;
    private String fee_volume_currency;
    private int margin_call;
    private int margin_stop;
}