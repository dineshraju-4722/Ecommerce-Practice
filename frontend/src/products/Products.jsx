import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchCategories, fetchProducts } from "../store/actions";
import Loader from "../shared/Loader";
import { FaExclamationTriangle } from "react-icons/fa";
import Paginations from "../shared/Paginations";
import useProductFilter from "../hooks/useProductFilter";
import ProductCard from "./ProductCard";

const Products = () => {
    const { isLoading, errorMessage } = useSelector((state) => state.errors);
    const { products, categories, pagination } = useSelector((state) => state.products);
    const dispatch = useDispatch();

    useProductFilter();
    useEffect(() => {
        dispatch(fetchCategories());
    }, [dispatch])
    return (
        <div>
            {isLoading ?
                (<Loader />
                ) : errorMessage ? (
                    // className="flex justify-center items-center h-[200px]"
                    <div >
                        {/* className="text-slate-800 text-3xl mr-2" */}
                        <FaExclamationTriangle />
                        {/* className="text-slate-800 text-lg font-medium" */}
                        <span >
                            {errorMessage}
                        </span>
                    </div>
                ) : (
                    // className="min-h-[700px]"
                    <div >
                        {/* className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6" */}
                        <div >
                            {products &&
                                products.map((item, i) => <ProductCard key={i} {...item} />
                                )}
                        </div>
                        {/* className="flex justify-center pt-10" */}
                        <div >
                            <Paginations
                                numberOfPage={pagination?.totalPages}
                                totalProducts={pagination?.totalElements} />
                        </div>
                    </div>
                )}
        </div>
    )
}

export default Products;

