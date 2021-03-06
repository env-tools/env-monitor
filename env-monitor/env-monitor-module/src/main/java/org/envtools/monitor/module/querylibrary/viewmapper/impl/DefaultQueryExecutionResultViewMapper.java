package org.envtools.monitor.module.querylibrary.viewmapper.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.envtools.monitor.common.util.ExceptionReportingUtil;
import org.envtools.monitor.model.querylibrary.execution.QueryExecutionResult;
import org.envtools.monitor.module.querylibrary.viewmapper.ColumnValueFormatter;
import org.envtools.monitor.model.querylibrary.execution.view.ColumnView;
import org.envtools.monitor.model.querylibrary.execution.view.QueryExecutionResultView;
import org.envtools.monitor.module.querylibrary.viewmapper.QueryExecutionResultViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created: 3/25/16 1:32 AM
 *
 * @author Yury Yakovlev
 */
@Service
public class DefaultQueryExecutionResultViewMapper implements QueryExecutionResultViewMapper {

    @Autowired
    private ColumnValueFormatter columnValueFormatter;

    @Override
    public QueryExecutionResultView map(QueryExecutionResult queryExecutionResult) {

        QueryExecutionResultView view = new QueryExecutionResultView();

        view.setStatus(String.valueOf(queryExecutionResult.getStatus()));

        if (queryExecutionResult.getErrorMessage().isPresent()) {
            view.setMessage(queryExecutionResult.getErrorMessage().get());
        }

        if (queryExecutionResult.getError().isPresent()) {
            view.setDetails(
                    ExceptionUtils.getFullStackTrace(
                            queryExecutionResult.getError().get())
            );
        }

        mapRows(view, queryExecutionResult);

        return view;

    }

    @Override
    public QueryExecutionResultView errorResult(Throwable t) {

        QueryExecutionResultView view = new QueryExecutionResultView();
        view.setStatus(String.valueOf(QueryExecutionResult.ExecutionStatusE.ERROR));
        if (t != null) {
            view.setMessage("Error occurred : " + ExceptionReportingUtil.getExceptionMessage(t));
            view.setDetails(ExceptionUtils.getFullStackTrace(t));
        } else {
            view.setMessage("Error occurred ");
            view.setDetails("No details provided");
        }

        return view;
    }

    @Override
    public QueryExecutionResultView cancelledResult() {
        QueryExecutionResultView view = new QueryExecutionResultView();
        view.setStatus(String.valueOf(QueryExecutionResult.ExecutionStatusE.CANCELLED));
        view.setMessage("Query cancelled");
        view.setDetails("Query cancelled");

        return view;
    }

    @Override
    public QueryExecutionResultView timeoutResult() {
        QueryExecutionResultView view = new QueryExecutionResultView();
        view.setStatus(String.valueOf(QueryExecutionResult.ExecutionStatusE.TIMED_OUT));
        view.setMessage("Query timed out");
        view.setDetails("Query timed out");

        return view;
    }

    private void mapRows(QueryExecutionResultView view, QueryExecutionResult queryExecutionResult) {
        List<ColumnView> columnsView = Lists.newArrayList();
        List<Map<String, String>> resultRowsView = Lists.newArrayList();

        List<Map<String, Object>> resultRows = queryExecutionResult.getResultRows();

        if (queryExecutionResult.getResultColumns() != null) {
            for (String column : queryExecutionResult.getResultColumns()) {
                columnsView.add(new ColumnView(column, column));
            }
        }

        for (Map<String, Object> row : resultRows) {

            Map<String, String> resultRowView = Maps.newLinkedHashMap();

            for (Map.Entry<String, Object> col : row.entrySet()) {
                resultRowView.put(col.getKey(), columnValueFormatter.formatColumnValue(col.getValue()));
            }

            resultRowsView.add(resultRowView);
        }

        view.setResult(resultRowsView);
        view.setColumns(columnsView);
    }


}
