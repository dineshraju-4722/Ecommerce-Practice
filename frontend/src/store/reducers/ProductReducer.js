const initialstate = {
    products: null,
    categories: null,
    pagination: {}
}

export const ProductReducer = (state = initialstate, action) => {
    switch (action.type) {
        case "FETCH_PRODUCTS":
            console.log(action.payload)
            return {
                ...state,
                products: action.payload,
                pagination: {
                    ...state.pagination,
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
                    ...state.pagination,
                    totalElements : action.totalElements , 
                    lastPage : action.lastPage,
                    totalPages : action.totalPages,
                    pageSize : action.pageSize,
                    pageNumber : action.pageNumber,
                },
            }
        default:
            return state;
    }
}