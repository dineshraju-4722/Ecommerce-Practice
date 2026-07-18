const initialstate = {
    isLoading: false,
    errorMessage: null,
    categoryLoader: false,
    categoryError: null,
    btnLoader: false,
}

export const ErrorReducer = (state = initialstate, action) => {
    switch (action.type) {
        case "IS_FETCHING":
            return {
                ...state,
                isLoading: true,
                errorMessage: null
            }
        case "IS_SUCCESS":
            return {
                ...state,
                isLoading: false,
                categoryError: null,
                errorMessage: null,
                categoryLoader: false,
                btnLoader: false,
            }
        case "BUTTON_LOADER":
            return {
                ...state,
                btnLoader: true,
                categoryError: null,
                errorMessage: null,
            }
        case "IS_ERROR":
            return {
                ...state,
                isLoading: false,
                errorMessage: action.payload,
                btnLoader: false,
                categoryLoader: false,
            }
        case "CATEGORY_SUCCESS":
            return {
                ...state,
                categoryLoader: false,
                categoryError: null,
            };
        case "CATEGORY_LOADER":
            return {
                ...state,
                categoryLoader: true,
                categoryError: null,
                errorMessage: null,
            }

        default:
            return state;
    }
}