package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.Page;
import com.micro.services.search.api.response.Pagination;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("paginationDelegate")
public class PaginationDelegate  extends BaseDelegate {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationDelegate.class);

//    @Value("${service.defaultRows}")
//    private int rows;


    @Value("${service.pagination.first.size}")
    private int numberOfFirstPages;

    @Value("${service.pagination.last.size}")
    private int numberOfLastPages;

    @Value("${service.pagination.next.size}")
    private int numberOfNextPages;

    @Value("${service.pagination.previous.size}")
    private int numberOfPreviousPages;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        long recordOffset = searchServiceRequest.getStart();
        long totalNumberOfRecords = searchServiceResponse.getNumFound();
        long numberOfRecordsPerPage = getRows(
                searchServiceRequest.getRows(),
                getRows(searchServiceRequest));
        long totalNumberOfPages = Math.round(
                Math.ceil((double) totalNumberOfRecords / (double) numberOfRecordsPerPage));
        long actualNumberOfNextPages = Math.min(
                numberOfNextPages,
                Math.round(Math.ceil(
                        (double) (totalNumberOfRecords - recordOffset) / (double) numberOfRecordsPerPage)) + 1);
        long actualNumberOfFirstPages = Math.min(numberOfFirstPages, totalNumberOfPages);
        long actualNumberOfLastPages = Math.min(numberOfLastPages, totalNumberOfPages);
        long actualNumberOfPreviousPages = Math.min(
                numberOfPreviousPages,
                Math.round(Math.ceil((double) recordOffset / (double) numberOfRecordsPerPage)));
        long activePageNumber = Math.round(Math.ceil((double) (recordOffset) / numberOfRecordsPerPage) + 1);


        String url = searchServiceResponse.getOriginalQuery();

        Pagination pagination = new Pagination();
        pagination.setNextPageList(buildPageList(activePageNumber,
                actualNumberOfNextPages + activePageNumber,
                activePageNumber,
                numberOfRecordsPerPage,
                totalNumberOfRecords,
                url));
        pagination.setPreviousPageList(buildPageList(activePageNumber - actualNumberOfPreviousPages,
                activePageNumber,
                activePageNumber,
                numberOfRecordsPerPage,
                totalNumberOfRecords,
                url));
        pagination.setFirstPageList(buildPageList(1,
                actualNumberOfFirstPages,
                activePageNumber,
                numberOfRecordsPerPage,
                totalNumberOfRecords,
                url));
        pagination.setLastPageList(buildPageList(totalNumberOfPages - actualNumberOfLastPages,
                totalNumberOfPages,
                activePageNumber,
                numberOfRecordsPerPage,
                totalNumberOfRecords,
                url));
        pagination.setActivePageNumber(activePageNumber);
        pagination.setTotalNumberOfPages(totalNumberOfPages);
        pagination.setNumberOfRecordsPerPage(numberOfRecordsPerPage);

        searchServiceResponse.setPagination(pagination);
        return searchServiceResponse;
    }





    public List<Page> buildPageList(long startIndexInput,
                                    long endIndexInput,
                                    long activePageNumber,
                                    long numberOfRecordsPerPage,
                                    long totalNumberOfPages,
                                    String url) {
        List<Page> pageList = new ArrayList<Page>();
        long startIndex = startIndexInput;
        long endIndex = endIndexInput;
        if (startIndexInput < 0) {
            startIndex = 0;
        }
        if (startIndexInput < 0) {
            endIndex = 0;
        }
        if (endIndexInput > totalNumberOfPages) {
            startIndex = totalNumberOfPages;
        }
        if (endIndexInput > totalNumberOfPages) {
            endIndex = totalNumberOfPages;
        }

        if (startIndex <= endIndex) {
            for (long pageIndex = startIndex; pageIndex <= endIndex; ++pageIndex) {
                Page page = new Page();
                page.setActive(pageIndex == activePageNumber);
                page.setNumber(pageIndex);
                page.setUrl(url
                        + GlobalConstants.AMPERSAND
                        + GlobalConstants.START
                        + GlobalConstants.EQUAL
                        + ((pageIndex - 1) * numberOfRecordsPerPage));
                pageList.add(page);
            }
        }
        return pageList;
    }
}

