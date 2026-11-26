package chetan.TechnicalAssessment.Models;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RebalancingResult {
    private final Map<String, Trade> trades = new LinkedHashMap<>();

    public RebalancingResult(List<Trade> tradeList) {
        for (Trade trade : tradeList) {
            trades.put(trade.getName(), trade);
        }
    }

    public Map<String, Trade> getTrades() {
        return Collections.unmodifiableMap(trades);
    }

    public Trade getTrade(String name) {
        return trades.get(name.trim().toUpperCase());
    }
}
