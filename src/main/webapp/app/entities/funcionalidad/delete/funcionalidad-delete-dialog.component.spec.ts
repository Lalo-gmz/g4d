jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FuncionalidadService } from '../service/funcionalidad.service';

import { FuncionalidadDeleteDialogComponent } from './funcionalidad-delete-dialog.component';

describe('Funcionalidad Management Delete Component', () => {
  let comp: FuncionalidadDeleteDialogComponent;
  let fixture: ComponentFixture<FuncionalidadDeleteDialogComponent>;
  let service: FuncionalidadService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FuncionalidadDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(FuncionalidadDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FuncionalidadDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FuncionalidadService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
