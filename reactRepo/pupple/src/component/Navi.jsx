import React, { useState } from 'react';
import styled from 'styled-components';
import NoticeList from './board/notice/NoticeList';
import { Link, useNavigate } from 'react-router-dom';

const StyledNaviDiv = styled.div`
    width: 100%;
    height: 60px;
    background-color: #C8ADFF;
    color: black;
    display: grid;
    grid-template-rows: 1fr;
    grid-template-columns: 1fr;
    grid-auto-columns: 1fr;
    grid-auto-flow: column;
    place-items: center center;
    font-size: 25px;
    margin: 20px;
`;

const StyledMenuItem = styled.div`
    position: relative;
    cursor: pointer;
    
    
    &:hover {
        color: #DBE76D;
    }
     
    & > div:nth-child(2){
        font-size: 15px;
        text-align: center;
        padding-top: 15px;
    }
    & > div:nth-child(2) :hover {
        color: #292929;
        border-bottom: 1px solid #292929;
    }
`;

const StyledSubMenu = styled.div`
    color: #C7C7C7;
    display: ${props => props.isopen ? 'block' : 'none'};
    position: absolute;
    top: 100%;
    left: 0;
`;

const Navi = () => {
    const [isNewsAdoptionOpen, setIsNewsAdoptionOpen] = useState(false);
    const navigate = useNavigate();

    const toggleNewsAdoption = () => {
        setIsNewsAdoptionOpen(!isNewsAdoptionOpen);
    };



    const handleVisitReservationClick = () => {
        const isLoggedIn = sessionStorage.getItem("loginMemberVo") !== null;
        if (isLoggedIn) {
            navigate("/board/visit/reservation");
        } else {
            alert("로그인이 필요한 서비스입니다.");
            
        }
    };




    return (
        <StyledNaviDiv>
            <StyledMenuItem onClick={toggleNewsAdoption}>
                <NoticeList />
                <StyledSubMenu isopen={isNewsAdoptionOpen}>
                    <div><Link to="/board/adoptionNews/list">입양 후 소식</Link></div>
                    <div><Link to="/board/law/list">관련법규</Link></div>
                </StyledSubMenu>
            </StyledMenuItem>
            <StyledMenuItem onClick={toggleNewsAdoption}>
                <div>입양하기</div>
                <StyledSubMenu isopen={isNewsAdoptionOpen}>
                    <div><Link to="/board/adoption/list">입양신청</Link></div>
                    <div><Link to="/board/adoptionOk/list">입양완료</Link></div>
                </StyledSubMenu>
            </StyledMenuItem>
            <StyledMenuItem>
                <div><Link to="/board/report/list">제보게시판</Link></div>
            </StyledMenuItem>
            <StyledMenuItem >
                <div onClick={handleVisitReservationClick}><Link>방문예약</Link></div>
            </StyledMenuItem>   
        </StyledNaviDiv>
    );
};

export default Navi;
