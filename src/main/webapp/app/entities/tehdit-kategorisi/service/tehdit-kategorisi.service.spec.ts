import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITehditKategorisi, TehditKategorisi } from '../tehdit-kategorisi.model';

import { TehditKategorisiService } from './tehdit-kategorisi.service';

describe('TehditKategorisi Service', () => {
  let service: TehditKategorisiService;
  let httpMock: HttpTestingController;
  let elemDefault: ITehditKategorisi;
  let expectedResult: ITehditKategorisi | ITehditKategorisi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TehditKategorisiService);
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

    it('should create a TehditKategorisi', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TehditKategorisi()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TehditKategorisi', () => {
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

    it('should partial update a TehditKategorisi', () => {
      const patchObject = Object.assign({}, new TehditKategorisi());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TehditKategorisi', () => {
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

    it('should delete a TehditKategorisi', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTehditKategorisiToCollectionIfMissing', () => {
      it('should add a TehditKategorisi to an empty array', () => {
        const tehditKategorisi: ITehditKategorisi = { id: 123 };
        expectedResult = service.addTehditKategorisiToCollectionIfMissing([], tehditKategorisi);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tehditKategorisi);
      });

      it('should not add a TehditKategorisi to an array that contains it', () => {
        const tehditKategorisi: ITehditKategorisi = { id: 123 };
        const tehditKategorisiCollection: ITehditKategorisi[] = [
          {
            ...tehditKategorisi,
          },
          { id: 456 },
        ];
        expectedResult = service.addTehditKategorisiToCollectionIfMissing(tehditKategorisiCollection, tehditKategorisi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TehditKategorisi to an array that doesn't contain it", () => {
        const tehditKategorisi: ITehditKategorisi = { id: 123 };
        const tehditKategorisiCollection: ITehditKategorisi[] = [{ id: 456 }];
        expectedResult = service.addTehditKategorisiToCollectionIfMissing(tehditKategorisiCollection, tehditKategorisi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tehditKategorisi);
      });

      it('should add only unique TehditKategorisi to an array', () => {
        const tehditKategorisiArray: ITehditKategorisi[] = [{ id: 123 }, { id: 456 }, { id: 57323 }];
        const tehditKategorisiCollection: ITehditKategorisi[] = [{ id: 123 }];
        expectedResult = service.addTehditKategorisiToCollectionIfMissing(tehditKategorisiCollection, ...tehditKategorisiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tehditKategorisi: ITehditKategorisi = { id: 123 };
        const tehditKategorisi2: ITehditKategorisi = { id: 456 };
        expectedResult = service.addTehditKategorisiToCollectionIfMissing([], tehditKategorisi, tehditKategorisi2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tehditKategorisi);
        expect(expectedResult).toContain(tehditKategorisi2);
      });

      it('should accept null and undefined values', () => {
        const tehditKategorisi: ITehditKategorisi = { id: 123 };
        expectedResult = service.addTehditKategorisiToCollectionIfMissing([], null, tehditKategorisi, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tehditKategorisi);
      });

      it('should return initial array if no TehditKategorisi is added', () => {
        const tehditKategorisiCollection: ITehditKategorisi[] = [{ id: 123 }];
        expectedResult = service.addTehditKategorisiToCollectionIfMissing(tehditKategorisiCollection, undefined, null);
        expect(expectedResult).toEqual(tehditKategorisiCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
