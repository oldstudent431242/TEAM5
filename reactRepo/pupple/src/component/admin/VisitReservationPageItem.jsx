import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const StyeldVisitReservationPageItemDiv = styled.div`
    width: 100%;
    height: 8%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 3% 0 3% 0;

    .pageNumber {
        padding: 0 5px 0 5px;
        margin: 5px 5px 5px 5px;;
    }
    .selectPage {
        padding: 0 5px 0 5px;
        margin: 5px 5px 5px 5px;;
        border: 1px solid #cfcfcf;
        color: #000000;
    }
`;

const VisitReservationPageItem = ( {pvo} ) => {


    return (
        <StyeldVisitReservationPageItemDiv>

            {/* 여기는 이전 버튼 !!! */}
            {pvo.startPage !== 1 && (
                <Link to={`/admin/visitReservation?pno=${pvo.startPage - 1}`}>이전</Link>
            )}
            

            {/* 여기는 페이징~~ */}
            {Array.from({ length: pvo.endPage - pvo.startPage + 1 }, (_, index) => {

                const pageNumber = pvo.startPage + index;

                return (

                    <div key={pageNumber}>

                        {
                            pageNumber === pvo.currentPage 
                            ? 
                                ( <div className='selectPage'>{pageNumber}</div>) 
                            : 
                                ( <Link className='pageNumber' to={`/admin/visitReservation?pno=${pageNumber}`}>{pageNumber}</Link> )
                        }
                        
                    </div>

                );
            })}


            {/* 여기는 다음 버튼 !!! */}
            {pvo.endPage !== pvo.maxPage && (
                <Link to={`/admin/visitReservation?pno=${pvo.endPage + 1}`}>다음</Link>
            )}

        </StyeldVisitReservationPageItemDiv>
    );
};

export default VisitReservationPageItem;