package com.mgiandia.library.domain;

import java.util.List;

public interface ItemService {
    List<Item> getItemsByBookno(Integer bookno);
}
