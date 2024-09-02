package io.lightplugins.inventory.module.config;

import io.lightplugins.inventory.module.LightInv;
import io.lightplugins.inventory.util.NumberFormatter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class SettingParams {

    private final LightInv lightInv;
    private final String defaultCurrency = "default-currency.";

    public SettingParams(LightInv lightEco) {
        this.lightInv = lightEco;
    }

    public String getModuleLanguage() {
        return lightInv.getSettings().getConfig().getString("module-language");
    }

    public DefaultCurrency defaultCurrency() {
        return new DefaultCurrency();
    }
    public SettingWrapper mainSettings() {
        return new SettingWrapper();
    }


    public class SettingWrapper {
        public SimpleDateFormat getDateFormat() {
            String result = lightInv.getSettings().getConfig().getString("module-language");
            return result != null ? new SimpleDateFormat(result) : new SimpleDateFormat("dd:MM:yyyy");
        }
    }

    public class DefaultCurrency {
        public BigDecimal getStartBalance() {
            double startBalance = lightInv.getSettings().getConfig().getDouble(defaultCurrency + "start-balance");
            return NumberFormatter.formatBigDecimal(BigDecimal.valueOf(startBalance));
        }
        public boolean firstJoinMessageEnabled() {
            return lightInv.getSettings().getConfig().getBoolean(defaultCurrency + "enable-first-join-message");
        }
        public String currencyPluralName() {
            return lightInv.getSettings().getConfig().getString(defaultCurrency + "currency-name-plural");
        }
        public String currencySingularName() {
            return lightInv.getSettings().getConfig().getString(defaultCurrency + "currency-name-singular");
        }
        public Integer fractionalDigits() {
            return lightInv.getSettings().getConfig().getInt(defaultCurrency + "fractional-digits");
        }
        public double maxPocketBalance() {
            return lightInv.getSettings().getConfig().getDouble(defaultCurrency + "max-pocket-balance");
        }
    }
}
