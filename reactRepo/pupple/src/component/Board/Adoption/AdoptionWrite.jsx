import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const StyledAdoptionWriteDiv = styled.div`
    width: 100%;
    height: 100%;
`;

const AdoptionWrite = ( ) => {

    let isFetching = false;

    const navigate = useNavigate(); 

    const [fileObj , setFileObj] = useState();
    const [dogName , setDogName] = useState();
    const [breed , setBreed] = useState();
    const [genderMf , setGenderMf] = useState();
    const [neuteringOx , setNeuteringOx] = useState();
    const [age , setAge] = useState();
    const [weight , setWeight] = useState();
    // const [rescueDogNo, setRescueDogNo] = useState();

    // const [formData, setFormData] = useState({
    //     dogName: '',
    //     breed: '',
    //     gender: '',
    //     neutering: '',
    //     age: '',
    //     weight: '',
    //     coatColor: '',
    // });

    // const handleChange = (e) => {
    //     const { name, value } = e.target;
    //     setFormData({ ...formData, [name]: value });
    // };

    const handleChangeFile = (e) => {
        setFileObj(e.target.files[0]);
    };
    const handleChangeDogName = (e) => {
        setDogName(e.target.value);
    };
    const handleChangeBreed= (e) => {
        setBreed(e.target.value);
    };
    const handleChangeGender= (e) => {
        setGenderMf(e.target.value);
    };
    const handleChangeNeutering = (e) => {
        setNeuteringOx(e.target.value);
    };
    const handleChangeAge = (e) => {
        setAge(e.target.value);
    };
    const handleChangeWeight = (e) => {
        setWeight(e.target.value);
    };

    // const handleChangeRescueDogNo = (e) => {
    //     setRescueDogNo(e.target.value);
    // }

    // const jsonStr = sessionStorage.getItem("adminVo");
    // const sessionAdminVo = JSON.parse(jsonStr);
    // const [adminVo , setAdminVo] = useState(sessionAdminVo);


    const handleSubmit = (e) => {
        e.preventDefault();


        // < isFetching >
        // 게시글 작성이 겹치면 이미지 시퀀스를 호출하는 과정에서 
        // 다른 게시글의 이미지 넘버를 불러올 수 있음
        //   ㄴSELECT SEQ_MEMBER_NO.CURRVAL FROM IMAGE
        if (isFetching) {
            return;
        }
        isFetching = true;

        


        const formData = new FormData();
        // formData.append("rescueDogNo", rescueDogNo);
        formData.append('file', fileObj);
        formData.append('dpgName', dogName);
        formData.append('breed', breed);
        formData.append('genderMf', genderMf);
        formData.append('neuteringOx', neuteringOx);
        formData.append('age', age);
        formData.append('weight', weight);
        // formData.append('rescueDogNo', rescueDogNo);
        // formData.append('adminNo', adminNo);


        fetch('http://127.0.0.1:8080/app/adoption/write' , {
            method: 'POST' ,
            body: formData ,
        })
        .then( (resp) => resp.json() )
        .then( (data) => {
            if (data.imgMsg === 'img insert good') {
                if (data.dogMsg === 'dog insert good') {
                    alert('게시글 등록 완료하였습니다.');
                    navigate('/board/adoption/list');
                    if (data.boardMsg === 'board write good') {
                        alert('게시글 등록 완료하였습니다.');
                        navigate('/board/adoption/list');
                        console.log("이미지오류로 작성싫=패 : " , data.imgMsg );
                    } else {
                        alert('게시글 등록 실패하였습니다.');
                        navigate("/board/adoption/write");
                        console.log("유기견오류로 작성싫=패 : " , data.dogMsg );
                    }
                } else {
                    alert('게시글 등록 실패하였습니다.');
                    navigate("/board/adoption/write");
                    console.log("게시글오류로 작성싫=패 : " , data.boardMsg );
                }
             } else {
                 alert("이미지 업로드 실패");
                 navigate("/board/adoption/write");
             }
        } )
        ;
    };


    return (
        <StyledAdoptionWriteDiv>
            <div className="adoption-write">
                <h1>입양 글 작성</h1>
                <form onSubmit={handleSubmit}>
                    <input type="file" className="file" onChange={handleChangeFile} />
                    <br />
                    <label>
                    이름:
                    <input type="text" className="dogName" onChange={handleChangeDogName} />
                    </label>
                    <label>
                    견종:
                    <input type="text" className="breed" onChange={handleChangeBreed} />
                    </label>
                    <label>
                    성별:
                    <input type="text" className="genderMf" onChange={handleChangeGender} />
                    </label>
                    <label>
                    중성화:
                    <input type="text" className="neuteringOx" onChange={handleChangeNeutering} />
                    </label>
                    <label>
                    나이:
                    <input type="number" className="age" onChange={handleChangeAge} />
                    </label>
                    <label>
                    몸무게:
                    <input type="number" className="weight" onChange={handleChangeWeight} />
                    </label>
                    <button type="submit">작성</button>
                </form>
            </div>
        </StyledAdoptionWriteDiv>
    );
};

export default AdoptionWrite;