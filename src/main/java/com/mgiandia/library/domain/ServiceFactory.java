package com.mgiandia.library.domain;

import jakarta.enterprise.inject.spi.CDI;

import com.mgiandia.library.domain.ItemService;
import com.mgiandia.library.domain.BorrowerService;
import com.mgiandia.library.domain.BookService;
import com.mgiandia.library.domain.BorrowerCardService;
import com.mgiandia.library.domain.AuthorService;

public class ServiceFactory {

    public static boolean isContainerAvailable() {
        try {
            CDI.current();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static ItemService getItemService() {
        return CDI.current().select(ItemService.class).get();
    }

    public static BorrowerService getBorrowerService() {
        return CDI.current().select(BorrowerService.class).get();
    }

    public static BookService getBookService() {
        return CDI.current().select(BookService.class).get();
    }

    public static BorrowerCardService getBorrowerCardService() {
        return CDI.current().select(BorrowerCardService.class).get();
    }

    public static AuthorService getAuthorService() {
        return CDI.current().select(AuthorService.class).get();
    }

}
