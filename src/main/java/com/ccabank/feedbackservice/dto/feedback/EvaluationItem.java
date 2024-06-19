package com.ccabank.feedbackservice.dto.feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvaluationItem {

    private String  element;

    private String  label;

    private int count;

    private float pourcent;

    private List<CountNoteDto> countNoteDtos = new ArrayList<CountNoteDto>();

    public Optional<CountNoteDto> getCountNoteDtoByNote(String note){
        return this.countNoteDtos.stream().filter(obj -> note.equals(obj.getNote())).findFirst();
    }

    public void addNote(String note){
        Optional<CountNoteDto> optional =  this.getCountNoteDtoByNote(note);
        CountNoteDto countNoteDto = new CountNoteDto();
        if(optional.isEmpty()){
            countNoteDto.setNote(note);
            countNoteDto.setCount(1);
            this.countNoteDtos.add(countNoteDto);
            return;
        }
        countNoteDto = optional.get() ;
        CountNoteDto countNoteDto2 = new CountNoteDto();
        countNoteDto2.setNote(countNoteDto.getNote());
        countNoteDto2.setCount(countNoteDto.getCount() + 1);
        this.countNoteDtos.remove(countNoteDto);
        this.countNoteDtos.add(countNoteDto2);
    }

    public List<CountNoteDto> getCountNoteDtos() {
        return countNoteDtos;
    }

    public void setCountNoteDtos(List<CountNoteDto> countNoteDtos) {
        this.countNoteDtos = countNoteDtos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getElement() {
        return element;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public float getPourcent() {
        return pourcent;
    }

    public void setPourcent(float pourcent) {
        this.pourcent = pourcent;
    }
}
