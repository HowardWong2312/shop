package io.dubai.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SymbolBalanceVO {

    private String symbol;

    private BigDecimal available;

    private BigDecimal freeze;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SymbolBalanceVO that = (SymbolBalanceVO) o;

        return symbol != null ? symbol.equals(that.symbol) : that.symbol == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        return result;
    }
}
