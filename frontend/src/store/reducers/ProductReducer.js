initialstate = {
    products: null,
    categories: null,
    pagination: {}
}

export const ProductReducer = (state = initialstate, action) => {
    switch (action.type) {
        case "FETCH_PRODUCTS":
            return {
                ...state,
                products: action.payload,
                pagination: {
                    ...pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage
                },  
            }
        case "FETCH_CATEGORIES":
            return {
                ...state,
                categories : action.payload,
                pagination : {
                    ...pagination,
                    totalElements : action.totalElements , 
                    lastPage = action.lastPage,
                    totalPages = action.totalPages,
                    pageSize : action.pageSize,
                    pageNumber : action.pageNumber,
                },
            }
        default:
            return state;
    }
}