import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVarlikKategorisi, VarlikKategorisi } from '../varlik-kategorisi.model';

import { VarlikKategorisiService } from './varlik-kategorisi.service';

describe('VarlikKategorisi Service', () => {
  let service: VarlikKategorisiService;
  let httpMock: HttpTestingController;
  let elemDefault: IVarlikKategorisi;
  let expectedResult: IVarlikKategorisi | IVarlikKategorisi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VarlikKategorisiService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a VarlikKategorisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new VarlikKategorisi()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VarlikKategorisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VarlikKategorisi', () => {
      const patchObject = Object.assign(
        {
          aciklama: 'BBBBBB',
        },
        new VarlikKategorisi()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VarlikKategorisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a VarlikKategorisi', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVarlikKategorisiToCollectionIfMissing', () => {
      it('should add a VarlikKategorisi to an empty array', () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 123 };
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing([], varlikKategorisi);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(varlikKategorisi);
      });

      it('should not add a VarlikKategorisi to an array that contains it', () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 123 };
        const varlikKategorisiCollection: IVarlikKategorisi[] = [
          {
            ...varlikKategorisi,
          },
          { id: 456 },
        ];
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing(varlikKategorisiCollection, varlikKategorisi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VarlikKategorisi to an array that doesn't contain it", () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 123 };
        const varlikKategorisiCollection: IVarlikKategorisi[] = [{ id: 456 }];
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing(varlikKategorisiCollection, varlikKategorisi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(varlikKategorisi);
      });

      it('should add only unique VarlikKategorisi to an array', () => {
        const varlikKategorisiArray: IVarlikKategorisi[] = [{ id: 123 }, { id: 456 }, { id: 67643 }];
        const varlikKategorisiCollection: IVarlikKategorisi[] = [{ id: 123 }];
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing(varlikKategorisiCollection, ...varlikKategorisiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 123 };
        const varlikKategorisi2: IVarlikKategorisi = { id: 456 };
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing([], varlikKategorisi, varlikKategorisi2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(varlikKategorisi);
        expect(expectedResult).toContain(varlikKategorisi2);
      });

      it('should accept null and undefined values', () => {
        const varlikKategorisi: IVarlikKategorisi = { id: 123 };
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing([], null, varlikKategorisi, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(varlikKategorisi);
      });

      it('should return initial array if no VarlikKategorisi is added', () => {
        const varlikKategorisiCollection: IVarlikKategorisi[] = [{ id: 123 }];
        expectedResult = service.addVarlikKategorisiToCollectionIfMissing(varlikKategorisiCollection, undefined, null);
        expect(expectedResult).toEqual(varlikKategorisiCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
